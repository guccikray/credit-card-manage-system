package org.guccikray.creditcardmanagesystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;
import org.guccikray.creditcardmanagesystem.dto.TransactionDto;
import org.guccikray.creditcardmanagesystem.form.TopUpBalanceForm;
import org.guccikray.creditcardmanagesystem.form.TransactionForm;
import org.guccikray.creditcardmanagesystem.model.AuthUser;
import org.guccikray.creditcardmanagesystem.model.CreditCardModel;
import org.guccikray.creditcardmanagesystem.service.CreditCardService;
import org.guccikray.creditcardmanagesystem.validator.TopUpBalanceFormValidator;
import org.guccikray.creditcardmanagesystem.validator.TransactionFromValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user/card")
@Tag(name = "Операции пользователя с картами")
public class UserCreditCardController {

    private final CreditCardService creditCardService;
    private final TransactionFromValidator transactionFromValidator;
    private final TopUpBalanceFormValidator topUpBalanceFormValidator;

    @Autowired
    public UserCreditCardController(
        CreditCardService creditCardService,
        TransactionFromValidator transactionFromValidator,
        TopUpBalanceFormValidator topUpBalanceFormValidator
    ) {
        this.creditCardService = creditCardService;
        this.transactionFromValidator = transactionFromValidator;
        this.topUpBalanceFormValidator = topUpBalanceFormValidator;
    }


    @InitBinder("transactionForm")
    protected void setTransactionFromValidator(WebDataBinder binder) {
        binder.setValidator(transactionFromValidator);
    }

    @InitBinder("topUpBalanceForm")
    protected void setTopUpBalanceFormValidator(WebDataBinder binder) {
        binder.setValidator(topUpBalanceFormValidator);
    }

    @GetMapping("/all-cards")
    public Page<CreditCardModel> getAllCards(
        @AuthenticationPrincipal AuthUser authUser,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String unmasked,
        Pageable pageable
    ) {
        boolean shouldBeUnmasked = "true".equalsIgnoreCase(unmasked);

        if (CardStatus.valid(status)) {
            return creditCardService.findAllCardsByUserIdAndStatus(
                authUser.getId(),
                CardStatus.valueOf(status),
                pageable,
                shouldBeUnmasked
            );
        }

        return creditCardService.findAllCardsByUserId(
            authUser.getId(),
            pageable,
            shouldBeUnmasked
        );
    }

    @PostMapping("/create-card")
    public CreditCardModel createCard(@AuthenticationPrincipal AuthUser authUser) {
        return creditCardService.create(authUser.getId());
    }

    @GetMapping("/{lastFourDigits:\\d+}")
    public CreditCardModel getCard(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable Integer lastFourDigits,
        @RequestParam(required = false) String unmasked
    ) {
        boolean shouldBeUnmasked = "true".equalsIgnoreCase(unmasked);

        return creditCardService.findByLastFourDigitsWithMaskedParam(
            authUser.getId(),
            lastFourDigits,
            shouldBeUnmasked
        );
    }

    @PutMapping("/{lastFourDigits:\\d+}/block")
    public CreditCardModel changeCardStatus(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable Integer lastFourDigits
    ) {
        return creditCardService.blockCard(
            authUser.getId(),
            lastFourDigits
        );
    }

    @PostMapping("/transfer")
    public TransactionDto makeTransaction(
        @AuthenticationPrincipal AuthUser authUser,
        @Valid @RequestBody TransactionForm form
    ) {
        return creditCardService.performTransaction(
            authUser.getId(),
            form
        );
    }

    @PutMapping("/top-up-balance")
    public CreditCardModel topUpBalance(
        @AuthenticationPrincipal AuthUser authUser,
        @Valid @RequestBody TopUpBalanceForm form
    ) {
        return creditCardService.topUpBalance(
            authUser.getId(),
            form
        );
    }
}
