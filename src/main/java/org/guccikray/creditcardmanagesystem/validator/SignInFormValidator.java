package org.guccikray.creditcardmanagesystem.validator;

import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.form.SignInForm;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static org.guccikray.creditcardmanagesystem.constants.ValidationConstants.*;

@Component
public class SignInFormValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Objects.equals(clazz, SignInForm.class);
    }

    @Override
    public void validate(
        @NonNull Object target,
        @NonNull Errors errors
    ) {
        SignInForm form = (SignInForm) target;

        if (StringUtils.isBlank(form.getEmail()) || form.getEmail().contains(" ")) {
            errors.rejectValue(EMAIL_FIELD, EMPTY_EMAIL_OR_WHITESPACES);
            return;
        }

        if (StringUtils.isBlank(form.getPassword()) || form.getPassword().contains(" ")) {
            errors.rejectValue(PASSWORD_FIELD, EMPTY_PASSWORD_OR_WHITESPACES);
        }
    }
}
