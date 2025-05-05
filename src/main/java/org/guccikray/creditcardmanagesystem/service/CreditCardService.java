package org.guccikray.creditcardmanagesystem.service;

import jakarta.validation.constraints.NotNull;
import org.guccikray.creditcardmanagesystem.assembler.CreditCardModelAssembler;
import org.guccikray.creditcardmanagesystem.db.entity.CreditCard;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;
import org.guccikray.creditcardmanagesystem.dto.TransactionDto;
import org.guccikray.creditcardmanagesystem.exception.*;
import org.guccikray.creditcardmanagesystem.form.ChangeCardStatusForm;
import org.guccikray.creditcardmanagesystem.form.TopUpBalanceForm;
import org.guccikray.creditcardmanagesystem.form.TransactionForm;
import org.guccikray.creditcardmanagesystem.model.CreditCardModel;
import org.guccikray.creditcardmanagesystem.repository.CreditCardRepository;
import org.guccikray.creditcardmanagesystem.repository.UserRepository;
import org.guccikray.creditcardmanagesystem.util.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CreditCardService {

    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int INDEX_FOR_LAST_DIGITS = 12;
    private static final int YEARS_BEFORE_EXPIRATION = 4;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardModelAssembler creditCardModelAssembler;
    private final UserRepository userRepository;

    @Autowired
    public CreditCardService(
        CreditCardRepository creditCardRepository,
        CreditCardModelAssembler creditCardModelAssembler,
        UserRepository userRepository
    ) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardModelAssembler = creditCardModelAssembler;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreditCardModel create(@NotNull Long userId) {
        String plainCardNumber = generateCardNumber();
        Integer lastFourDigits = Integer.parseInt(plainCardNumber.substring(INDEX_FOR_LAST_DIGITS));

        String encryptedCardNumber;
        try {
            encryptedCardNumber = AesUtil.encrypt(plainCardNumber);
        } catch (Exception e) {
            throw new EncryptingDataException("Error while encypting data");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        CreditCard card = new CreditCard()
            .setCardNumber(encryptedCardNumber)
            .setLastFourDigits(lastFourDigits)
            .setUserId(userId)
            .setUser(optionalUser.get())
            .setCardStatus(CardStatus.ACTIVE)
            .setExpirationDate(LocalDate.now().plusYears(YEARS_BEFORE_EXPIRATION))
            .setBalance(0L);

        creditCardRepository.save(card);
        return creditCardModelAssembler.toModel(card, false);
    }

    public Page<CreditCardModel> findAllCardsByUserId(
        @NotNull Long userId,
        @NotNull Pageable pageable,
        boolean unmasked
    ) {
        Page<CreditCard> cards = creditCardRepository.findAllByUserId(userId, pageable);

        return creditCardModelAssembler.toPagedModel(cards, unmasked);
    }

    public Page<CreditCardModel> findAllCardsByUserIdAndStatus(
        @NotNull Long userId,
        @NotNull CardStatus status,
        @NotNull Pageable pageable,
        boolean unmasked
    ) {
        Page<CreditCard> cards = creditCardRepository.findAllByUserIdAndCardStatus(
            userId,
            status,
            pageable
        );

        return creditCardModelAssembler.toPagedModel(cards, unmasked);
    }

    public CreditCardModel findByLastFourDigitsWithMaskedParam(
        @NotNull Long userId,
        @NotNull Integer lastFourDigits,
        boolean unmasked
    ) {
        CreditCard card = findByLastFourDigits(
            userId,
            lastFourDigits
        );

        return creditCardModelAssembler.toModel(card, unmasked);
    }

    @Transactional
    public CreditCardModel blockCard(
        @NotNull Long userId,
        @NotNull Integer lastFourDigits
    ) {
        CreditCard card = findByLastFourDigits(
            userId,
            lastFourDigits
        );

        checkIfCardActive(card);
        card.setCardStatus(CardStatus.BLOCKED);
        return creditCardModelAssembler.toModel(card, false);
    }

    @Transactional
    public CreditCardModel changeCardStatus(
        @NotNull Long userId,
        @NotNull ChangeCardStatusForm form
    ) {
        String status = form.getStatus();

        if (!CardStatus.valid(status)) {
            throw new InvalidStatusException("Invalid card status");
        }

        CreditCard card = findByLastFourDigits(
            userId,
            Integer.parseInt(form.getLastFourDigits())
        );

        checkIfCardActive(card);
        card.setCardStatus(CardStatus.valueOf(status.toUpperCase()));

        return creditCardModelAssembler.toModel(card, false);
    }

    @Transactional
    public TransactionDto performTransaction(
        @NonNull Long userId,
        @NonNull TransactionForm form
    ) {
        Integer sourceCardLastFourDigits = Integer.parseInt(form.getSourceCardLastFourDigits());
        Integer destinationCardLastFourDigits = Integer.parseInt(form.getDestinationCardLastFourDigits());
        Long amount = Long.parseLong(form.getAmount());

        List<CreditCard> cards = creditCardRepository.findTwoCardsByUserIdAndLastFourDigitsIn(
            userId,
            List.of(sourceCardLastFourDigits, destinationCardLastFourDigits)
        );

        if (cards.size() != 2) {
            throw new CardNotFoundException("One or both cards last four digits are incorrect");
        }

        for (CreditCard card : cards) {
            checkIfCardActive(card);
        }

        CreditCard sourceCard = findByLastFourDigitsInCollection(cards, sourceCardLastFourDigits);
        CreditCard destinationCard = findByLastFourDigitsInCollection(cards, destinationCardLastFourDigits);

        if (sourceCard.getBalance().compareTo(amount) < 0) {
            throw new SourceCardDontHaveEnoughAmountException(
                "Source credit card do not have enough amount to transfer"
            );
        }

        Long sourceCardBalance = sourceCard.getBalance();
        Long destinationCardBalance = destinationCard.getBalance();

        sourceCard.setBalance(sourceCardBalance - amount);
        destinationCard.setBalance(destinationCardBalance + amount);

        return new TransactionDto()
            .setSourceCardNumber(String.valueOf(sourceCardLastFourDigits))
            .setSourceCardBalance(sourceCard.getBalance())
            .setDestinationCardNumber(String.valueOf(destinationCardLastFourDigits))
            .setDestinationCardBalance(destinationCard.getBalance())
            .setAmount(amount);

    }

    @Transactional
    public CreditCardModel topUpBalance(
        @NotNull Long userId,
        @NotNull TopUpBalanceForm form
    ) {
        Integer amount = Integer.parseInt(form.getAmount());

        CreditCard card = findByLastFourDigits(
            userId,
            Integer.parseInt(form.getCardLastFourDigits())
        );

        checkIfCardActive(card);
        Long currentBalance = card.getBalance();
        card.setBalance(currentBalance + amount);
        return creditCardModelAssembler.toModel(card, false);
    }

    public Page<CreditCardModel> findAllCard(@NotNull Pageable pageable) {
        Page<CreditCard> cards = creditCardRepository.findAll(pageable);

        return creditCardModelAssembler.toPagedModel(cards, false);
    }

    @Transactional
    public void deleteCard(
        @NotNull Long userId,
        @NotNull Integer lastFourDigits
    ) {
        CreditCard card = findByLastFourDigits(
            userId,
            lastFourDigits
        );

        creditCardRepository.delete(card);
    }

    private CreditCard findByLastFourDigits(
        @NotNull Long userId,
        @NotNull Integer lastFourDigits
    ) {
        Optional<CreditCard> optionalCard = creditCardRepository.findByUserIdAndLastFourDigits(
            userId,
            lastFourDigits
        );

        if (optionalCard.isEmpty()) {
            throw new CardNotFoundException("Card not found");
        }

        return optionalCard.get();
    }

    private CreditCard findByLastFourDigitsInCollection(
        @NotNull Collection<CreditCard> cards,
        @NotNull Integer cardLastFourDigits
    ) {
        Optional<CreditCard> optionalCard = cards.stream()
            .filter(card -> Objects.equals(card.getLastFourDigits(), cardLastFourDigits))
            .findFirst();

        if (optionalCard.isEmpty()) {
            throw new CardNotFoundException("Card not found");
        }

        return optionalCard.get();
    }

    private void checkIfCardActive(@NotNull CreditCard card) {
        if (CardStatus.ACTIVE != card.getCardStatus()) {
            throw new CardIsExpiredException("Card is expired or blocked");
        }
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        return builder.toString();
    }

}
