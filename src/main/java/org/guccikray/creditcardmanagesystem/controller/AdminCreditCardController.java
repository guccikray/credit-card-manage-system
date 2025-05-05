package org.guccikray.creditcardmanagesystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.guccikray.creditcardmanagesystem.form.ChangeCardStatusForm;
import org.guccikray.creditcardmanagesystem.model.CreditCardModel;
import org.guccikray.creditcardmanagesystem.service.CreditCardService;
import org.guccikray.creditcardmanagesystem.validator.ChangeCardStatusFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/admin/card")
@Tag(name = "Aдминка. Операции с картами")
public class AdminCreditCardController {

    private final CreditCardService creditCardService;
    private final ChangeCardStatusFormValidator changeCardStatusFormValidator;

    @Autowired
    public AdminCreditCardController(
        CreditCardService creditCardService,
        ChangeCardStatusFormValidator changeCardStatusFormValidator
    ) {
        this.creditCardService = creditCardService;
        this.changeCardStatusFormValidator = changeCardStatusFormValidator;
    }

    @InitBinder("changeCardStatusForm")
    protected void setChangeCardStatusFormValidator(WebDataBinder binder) {
        binder.setValidator(changeCardStatusFormValidator);
    }

    @GetMapping("/all-cards")
    public Page<CreditCardModel> getAllCards(Pageable pageable) {
        return creditCardService.findAllCard(pageable);
    }

    @GetMapping("/{userId:\\d+}/all-cards")
    public Page<CreditCardModel> getAllByUserId(
        @PathVariable Long userId,
        Pageable pageable
    ) {
        return creditCardService.findAllCardsByUserId(
            userId,
            pageable,
            true
        );
    }

    @GetMapping("/{userId:\\d+}/{lastFourDigits:\\d+}")
    public CreditCardModel getCardByUserIdAndLastFourDigits(
        @PathVariable Long userId,
        @PathVariable Integer lastFourDigits
    ) {
        return creditCardService.findByLastFourDigitsWithMaskedParam(
            userId,
            lastFourDigits,
            true
        );
    }

    @PostMapping("/{userId:\\d+}/create-card")
    public CreditCardModel createCard(@PathVariable Long userId) {
        return creditCardService.create(userId);
    }

    @PutMapping("/{userId:\\d+}/change-status")
    public CreditCardModel changeCardStatus(
        @PathVariable Long userId,
        @Valid @RequestBody ChangeCardStatusForm form
    ) {
        return creditCardService.changeCardStatus(
            userId,
            form
        );
    }

    @DeleteMapping("/{userId:\\d+}/{lastFourDigits:\\d+}/delete")
    public String deleteCard(
        @PathVariable Long userId,
        @PathVariable Integer lastFourDigits
    ) {
        creditCardService.deleteCard(
            userId,
            lastFourDigits
        );

        return String.format(
            "Card with user id:%s and last four digits:%s successfully deleted",
            userId,
            lastFourDigits
        );
    }
}
