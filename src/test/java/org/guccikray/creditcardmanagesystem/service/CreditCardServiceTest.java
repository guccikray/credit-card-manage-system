package org.guccikray.creditcardmanagesystem.service;

import org.guccikray.creditcardmanagesystem.assembler.CreditCardModelAssembler;
import org.guccikray.creditcardmanagesystem.db.entity.CreditCard;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;
import org.guccikray.creditcardmanagesystem.db.enums.Role;
import org.guccikray.creditcardmanagesystem.dto.TransactionDto;
import org.guccikray.creditcardmanagesystem.exception.CardNotFoundException;
import org.guccikray.creditcardmanagesystem.exception.InvalidStatusException;
import org.guccikray.creditcardmanagesystem.exception.SourceCardDontHaveEnoughAmountException;
import org.guccikray.creditcardmanagesystem.form.ChangeCardStatusForm;
import org.guccikray.creditcardmanagesystem.form.TopUpBalanceForm;
import org.guccikray.creditcardmanagesystem.form.TransactionForm;
import org.guccikray.creditcardmanagesystem.model.CreditCardModel;
import org.guccikray.creditcardmanagesystem.repository.CreditCardRepository;
import org.guccikray.creditcardmanagesystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @Mock
    CreditCardRepository creditCardRepository;
    @Spy
    CreditCardModelAssembler creditCardModelAssembler;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    CreditCardService creditCardService;

    private static User user;
    private static CreditCard card;
    private static CreditCard anotherCard;
    private static ChangeCardStatusForm invalidChangeCardStatusForm;
    private static ChangeCardStatusForm validChangeCardStatusForm;
    private static TransactionForm transactionForm;
    private static TransactionForm anotherTransactionForm;
    private static TopUpBalanceForm topUpBalanceForm;

    @BeforeEach
    void init() {
        user = new User()
            .setName("name")
            .setSurname("surname")
            .setEmail("email")
            .setPassword("pass")
            .setRole(Role.ROLE_USER);
        user.setId(1L);

        card = new CreditCard()
            .setCardNumber("XsvSn58Eiy+Pjff2Q6zzmAcLGOLjxhWqsM4TU/x4xH0x3MwSqUNvXtasTMM=")
            .setLastFourDigits(7534)
            .setCardStatus(CardStatus.ACTIVE)
            .setUser(user)
            .setUserId(user.getId())
            .setBalance(0L)
            .setExpirationDate(LocalDate.now().plusYears(4));

        anotherCard = new CreditCard()
            .setCardNumber("mhIUX0emgtIR2+ZuESqpjRud9xn0gNohq1qF+2hc9wlwuQOyYR9GcIKBhZk=")
            .setLastFourDigits(2544)
            .setCardStatus(CardStatus.ACTIVE)
            .setUser(user)
            .setUserId(user.getId())
            .setBalance(1500L)
            .setExpirationDate(LocalDate.now().plusYears(4));

        invalidChangeCardStatusForm = new ChangeCardStatusForm()
            .setLastFourDigits(String.valueOf(card.getLastFourDigits()))
            .setStatus("Status");

        validChangeCardStatusForm = new ChangeCardStatusForm()
            .setLastFourDigits(String.valueOf(card.getLastFourDigits()))
            .setStatus("Blocked");

        transactionForm = new TransactionForm()
            .setSourceCardLastFourDigits(String.valueOf(card.getLastFourDigits()))
            .setDestinationCardLastFourDigits(String.valueOf(anotherCard.getLastFourDigits()))
            .setAmount("1000");

        anotherTransactionForm = new TransactionForm()
            .setSourceCardLastFourDigits(String.valueOf(anotherCard.getLastFourDigits()))
            .setDestinationCardLastFourDigits(String.valueOf(card.getLastFourDigits()))
            .setAmount("1000");

        topUpBalanceForm = new TopUpBalanceForm()
            .setCardLastFourDigits(String.valueOf(card.getLastFourDigits()))
            .setAmount("1000");
    }

    @Test
    void testCreateSuccessfully() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThat(creditCardService.create(user.getId())).isInstanceOf(CreditCardModel.class);
    }

    @Test
    void testBlockCardAndCardNotFound() {

        when(creditCardRepository.findByUserIdAndLastFourDigits(
            user.getId(),
            card.getLastFourDigits())
        ).thenReturn(Optional.empty());

        assertThatThrownBy(() -> creditCardService.blockCard(user.getId(), card.getLastFourDigits()))
            .isInstanceOf(CardNotFoundException.class)
            .hasMessage("Card not found");
    }

    @Test
    void testBlockCardSuccessfully() {

        when(creditCardRepository.findByUserIdAndLastFourDigits(
            user.getId(),
            card.getLastFourDigits()
            )
        ).thenReturn(Optional.of(card));

        assertEquals(
            CardStatus.BLOCKED,
            creditCardService.blockCard(user.getId(), card.getLastFourDigits()).getStatus()
        );
    }

    @Test
    void testChangeCardStatusAndStatusInvalid() {

        assertThatThrownBy(() -> creditCardService.changeCardStatus(user.getId(), invalidChangeCardStatusForm))
            .isInstanceOf(InvalidStatusException.class)
            .hasMessage("Invalid card status");
    }

    @Test
    void testChangeCardStatusSuccessfully() {

        when(creditCardRepository.findByUserIdAndLastFourDigits(
            user.getId(),
            card.getLastFourDigits())
        ).thenReturn(Optional.of(card));

        assertEquals(
            CardStatus.BLOCKED,
            creditCardService.changeCardStatus(user.getId(), validChangeCardStatusForm).getStatus()
        );
    }

    @Test
    void testPerformTransactionAndOneCardNumberIncorrect() {

        when(creditCardRepository.findTwoCardsByUserIdAndLastFourDigitsIn(
            user.getId(),
            List.of(
                Integer.parseInt(transactionForm.getSourceCardLastFourDigits()),
                Integer.parseInt(transactionForm.getDestinationCardLastFourDigits())
            )
        )).thenReturn(List.of(card));

        assertThatThrownBy(() -> creditCardService.performTransaction(user.getId(), transactionForm))
            .isInstanceOf(CardNotFoundException.class)
            .hasMessage("One or both cards last four digits are incorrect");
    }

    @Test
    void testPerformTransactionAndAmountIsBiggerThanBalance() {

        when(creditCardRepository.findTwoCardsByUserIdAndLastFourDigitsIn(
            user.getId(),
            List.of(
                Integer.parseInt(transactionForm.getSourceCardLastFourDigits()),
                Integer.parseInt(transactionForm.getDestinationCardLastFourDigits())
            )
        )).thenReturn(List.of(card, anotherCard));

        assertThatThrownBy(() -> creditCardService.performTransaction(user.getId(), transactionForm))
            .isInstanceOf(SourceCardDontHaveEnoughAmountException.class)
            .hasMessage("Source credit card do not have enough amount to transfer");
    }

    @Test
    void testPerformTransactionSuccessfully() {

        when(creditCardRepository.findTwoCardsByUserIdAndLastFourDigitsIn(
            user.getId(),
            List.of(
                Integer.parseInt(anotherTransactionForm.getSourceCardLastFourDigits()),
                Integer.parseInt(anotherTransactionForm.getDestinationCardLastFourDigits())
            )
        )).thenReturn(List.of(anotherCard, card));

        assertThat(creditCardService.performTransaction(
            user.getId(),
            anotherTransactionForm)
        ).isInstanceOf(TransactionDto.class);
    }

    @Test
    void testTopUpBalanceSuccessfully() {

        when(creditCardRepository.findByUserIdAndLastFourDigits(
            user.getId(),
            card.getLastFourDigits()
        )).thenReturn(Optional.of(card));

        assertEquals(
            Integer.parseInt(topUpBalanceForm.getAmount()),
            creditCardService.topUpBalance(user.getId(), topUpBalanceForm).getBalance()
        );
    }

    @Test
    void testDeleteCardSuccessfully() {

        when(creditCardRepository.findByUserIdAndLastFourDigits(
            user.getId(),
            card.getLastFourDigits()
        )).thenReturn(Optional.of(card));

        creditCardService.deleteCard(user.getId(), card.getLastFourDigits());
        verify(creditCardRepository, times(1)).delete(card);
    }
}
