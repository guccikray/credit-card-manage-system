package org.guccikray.creditcardmanagesystem.validator;

import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.form.ChangeCardStatusForm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static org.guccikray.creditcardmanagesystem.constants.ValidationConstants.*;

@Component
public class ChangeCardStatusFormValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(clazz, ChangeCardStatusForm.class);
    }

    @Override
    public void validate(
        @NonNull Object target,
        @NonNull Errors errors
    ) {
        ChangeCardStatusForm form = (ChangeCardStatusForm) target;

        if (StringUtils.isBlank(form.getLastFourDigits()) || form.getLastFourDigits().contains(" ")) {
            errors.rejectValue(LAST_FOUR_DIGITS_FIELD, EMPTY_LAST_DIGITS_OR_WHITESPACES);
            return;
        }

        if (!DIGITS.matcher(form.getLastFourDigits()).matches()) {
            errors.rejectValue(LAST_FOUR_DIGITS_FIELD, LAST_FOUR_DIGITS_CONTAINS_CHAR);
            return;
        }

        if (form.getLastFourDigits().length() != 4) {
            errors.rejectValue(LAST_FOUR_DIGITS_FIELD, LAST_FOUR_DIGITS_LENGTH);
            return;
        }

        if (StringUtils.isBlank(form.getStatus()) || form.getStatus().contains(" ")) {
            errors.rejectValue(STATUS_FIELD, EMPTY_STATUS_OR_WHITESPACES);
        }
    }
}
