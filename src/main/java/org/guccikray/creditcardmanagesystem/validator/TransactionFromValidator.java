package org.guccikray.creditcardmanagesystem.validator;

import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.form.TransactionForm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static org.guccikray.creditcardmanagesystem.constants.ValidationConstants.*;

@Component
public class TransactionFromValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(clazz, TransactionForm.class);
    }

    @Override
    public void validate(
        @NonNull Object target, 
        @NonNull Errors errors
    ) {
        TransactionForm form = (TransactionForm) target;
        
        if (StringUtils.isBlank(form.getSourceCardLastFourDigits()) 
            || form.getSourceCardLastFourDigits().contains(" ")
        ) {
            errors.rejectValue(SOURCE_CARD_FIELD, EMPTY_SOURCE_CARD_OR_WHITESPACES);
            return;
        }
        
        if (!DIGITS.matcher(form.getSourceCardLastFourDigits()).matches()) {
            errors.rejectValue(SOURCE_CARD_FIELD, SOURCE_CARD_CONTAINS_CHAR);
            return;
        }

        if (form.getSourceCardLastFourDigits().length() != 4) {
            errors.rejectValue(SOURCE_CARD_FIELD, LAST_FOUR_DIGITS_LENGTH);
            return;
        }
        
        if (StringUtils.isBlank(form.getDestinationCardLastFourDigits()) 
            || form.getDestinationCardLastFourDigits().contains(" ")
        ) {
            errors.rejectValue(DESTINATION_CARD_FIELD, EMPTY_DESTINATION_CARD_OR_WHITESPACES);
            return;
        }

        if (!DIGITS.matcher(form.getDestinationCardLastFourDigits()).matches()
        ) {
            errors.rejectValue(DESTINATION_CARD_FIELD, DESTINATION_CARD_CONTAINS_CHAR);
            return;
        }

        if (form.getDestinationCardLastFourDigits().length() != 4) {
            errors.rejectValue(DESTINATION_CARD_FIELD, LAST_FOUR_DIGITS_LENGTH);
            return;
        }
        
        if (StringUtils.isBlank(form.getAmount()) || form.getAmount().contains(" ")) {
            errors.rejectValue(AMOUNT_FIELD, EMPTY_AMOUNT_OR_WHITESPACES);
            return;
        }
        
        if (!INTEGER.matcher(form.getAmount()).matches()) {
            errors.rejectValue(AMOUNT_FIELD, AMOUNT_CONTAINS_CHAR);
            return;
        }

        if (Long.parseLong(form.getAmount()) <= 0) {
            errors.rejectValue(AMOUNT_FIELD, NEGATIVE_AMOUNT);
        }
    }
}
