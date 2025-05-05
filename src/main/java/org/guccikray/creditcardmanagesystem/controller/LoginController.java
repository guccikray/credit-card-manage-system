package org.guccikray.creditcardmanagesystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.guccikray.creditcardmanagesystem.db.enums.Role;
import org.guccikray.creditcardmanagesystem.form.RegisterUserForm;
import org.guccikray.creditcardmanagesystem.form.SignInForm;
import org.guccikray.creditcardmanagesystem.model.UserModel;
import org.guccikray.creditcardmanagesystem.service.UserService;
import org.guccikray.creditcardmanagesystem.validator.RegisterUserFormValidator;
import org.guccikray.creditcardmanagesystem.validator.SignInFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Aутентификация")
public class LoginController {

    private final RegisterUserFormValidator registerUserFormValidator;
    private final SignInFormValidator signInFormValidator;
    private final UserService userService;

    @Autowired
    public LoginController(
        RegisterUserFormValidator registerUserFormValidator,
        SignInFormValidator signInFormValidator,
        UserService userService
    ) {
        this.registerUserFormValidator = registerUserFormValidator;
        this.signInFormValidator = signInFormValidator;
        this.userService = userService;
    }

    @InitBinder("registerUserForm")
    protected void setRegisterUserFormValidator(WebDataBinder binder) {
        binder.setValidator(registerUserFormValidator);
    }

    @InitBinder("signInForm")
    protected void setSignInFormValidator(WebDataBinder binder) {
        binder.setValidator(signInFormValidator);
    }

    @PostMapping("/registration")
    public UserModel registration(@Valid @RequestBody RegisterUserForm form) {
        return userService.create(form, Role.ROLE_USER);
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid @RequestBody SignInForm form) {
        return userService.signIn(form);
    }
}
