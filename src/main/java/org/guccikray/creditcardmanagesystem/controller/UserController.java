package org.guccikray.creditcardmanagesystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.guccikray.creditcardmanagesystem.form.EditUserForm;
import org.guccikray.creditcardmanagesystem.model.AuthUser;
import org.guccikray.creditcardmanagesystem.model.UserModel;
import org.guccikray.creditcardmanagesystem.service.UserService;
import org.guccikray.creditcardmanagesystem.validator.EditUserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
@Tag(name = "Операции пользователя над самим собой")
public class UserController {

    private final EditUserFormValidator editUserFormValidator;
    private final UserService userService;

    @Autowired
    public UserController(
        EditUserFormValidator editUserFormValidator,
        UserService userService
    ) {
        this.editUserFormValidator = editUserFormValidator;
        this.userService = userService;
    }

    @InitBinder("editUserForm")
    protected void setEditUserFormValidator(WebDataBinder binder) {
        binder.setValidator(editUserFormValidator);
    }

    @GetMapping("/info")
    public UserModel getInfo(@AuthenticationPrincipal AuthUser authUser) {
        return userService.getUserInfo(authUser.getId());
    }

    @PutMapping("/edit-user")
    public UserModel editUser(
        @AuthenticationPrincipal AuthUser authUser,
        @Valid @RequestBody EditUserForm form
    ) {
        return userService.editUser(authUser.getId(), form, null);
    }
}
