package org.guccikray.creditcardmanagesystem.validator;

import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.form.EditUserForm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static org.guccikray.creditcardmanagesystem.constants.ValidationConstants.*;

@Component
public class EditUserFormValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(clazz, EditUserForm.class);
    }

    @Override
    public void validate(
        @NonNull Object target,
        @NonNull Errors errors
    ) {
        EditUserForm form = (EditUserForm) target;

        if (StringUtils.isNotBlank(form.getName()) && form.getName().contains(" ")) {
            errors.rejectValue(NAME_FIElD, EMPTY_NAME_OR_WHITESPACES);
            return;
        }

        if (StringUtils.isNotBlank(form.getSurname()) && form.getSurname().contains(" ")) {
            errors.rejectValue(SURNAME_FIELD, EMPTY_SURNAME_OR_WHITESPACES);
            return;
        }

        if (StringUtils.isNotBlank(form.getEmail()) && form.getEmail().contains(" ")) {
            errors.rejectValue(EMAIL_FIELD, EMPTY_EMAIL_OR_WHITESPACES);
            return;
        }

        if (StringUtils.isNotBlank(form.getEmail()) && !EMAIL_REGEX.matcher(form.getEmail()).matches()) {
            errors.rejectValue(EMAIL_FIELD, INVALID_EMAIL_FORMAT);
            return;
        }

        if (StringUtils.isNotBlank(form.getPassword()) && form.getPassword().contains(" ")) {
            errors.rejectValue(PASSWORD_FIELD, EMPTY_PASSWORD_OR_WHITESPACES);
            return;
        }

        if (StringUtils.isNotBlank(form.getPassword()) && !getPasswordRegex().matcher(form.getPassword()).matches()) {
            errors.rejectValue(PASSWORD_FIELD, PASSWORD_HAS_DISALLOWED_SEQUENCE);
        }
    }
}
