package org.guccikray.creditcardmanagesystem.assembler;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.db.entity.CreditCard;
import org.guccikray.creditcardmanagesystem.exception.DecryptingDataException;
import org.guccikray.creditcardmanagesystem.model.CreditCardModel;
import org.guccikray.creditcardmanagesystem.util.AesUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditCardModelAssembler extends AbstractModelAssembler<CreditCardModel, CreditCard> {

    @Override
    public CreditCardModel toModel(@NotNull CreditCard card) {
        int replacementLength = 12;
        String decryptedCardNumber = decryptCardNumber(card);
        String maskedCardNumber = StringUtils.repeat("*", replacementLength)
            + decryptedCardNumber.substring(replacementLength);

        return buildModel(card, maskedCardNumber);
    }


    public CreditCardModel toModel(
        @NotNull CreditCard card,
        boolean unmasked
    ) {
        if (!unmasked) {
            return toModel(card);
        }

        return buildModel(card, decryptCardNumber(card));
    }


    public List<CreditCardModel> toModels(
        @NotNull List<CreditCard> cards,
        boolean unmasked
    ) {
        if (!unmasked) {
            return toModels(cards);
        }

        return cards.stream()
            .map(card -> toModel(card, false))
            .toList();
    }

    public Page<CreditCardModel> toPagedModel(
        @NotNull Page<CreditCard> page,
        boolean unmasked
    ) {
        if (!unmasked) {
            return toPagedModel(page);
        }

        List<CreditCard> cards = page.getContent();
        return new PageImpl<>(
            toModels(cards, false),
            page.getPageable(),
            page.getTotalElements()
        );
    }

    private CreditCardModel buildModel(
        @NotNull CreditCard card,
        @NotNull String cardNumber
    ) {
        return new CreditCardModel()
            .setCardNumber(cardNumber)
            .setName(card.getUser().getName())
            .setSurname(card.getUser().getSurname())
            .setBalance(card.getBalance())
            .setUserId(card.getUserId())
            .setStatus(card.getCardStatus())
            .setExpirationDate(card.getExpirationDate());
    }

    private String decryptCardNumber(@NotNull CreditCard card) {

        String decryptedCardNumber;
        try {
            decryptedCardNumber = AesUtil.decrypt(card.getCardNumber());
        } catch (Exception e) {
            throw new DecryptingDataException("Error while decrypting data");
        }

        return decryptedCardNumber;
    }
}
