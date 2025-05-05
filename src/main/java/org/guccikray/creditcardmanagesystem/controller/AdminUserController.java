package org.guccikray.creditcardmanagesystem.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.guccikray.creditcardmanagesystem.db.enums.Role;
import org.guccikray.creditcardmanagesystem.form.EditUserForm;
import org.guccikray.creditcardmanagesystem.form.RegisterUserForm;
import org.guccikray.creditcardmanagesystem.model.UserModel;
import org.guccikray.creditcardmanagesystem.service.UserService;
import org.guccikray.creditcardmanagesystem.validator.EditUserFormValidator;
import org.guccikray.creditcardmanagesystem.validator.RegisterUserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/admin/user")
@Tag(name = "Админка. Операции с пользователями")
public class AdminUserController {

    private final UserService userService;
    private final RegisterUserFormValidator registerUserFormValidator;
    private final EditUserFormValidator editUserFormValidator;

    @Autowired
    public AdminUserController(
        UserService userService,
        RegisterUserFormValidator registerUserFormValidator,
        EditUserFormValidator editUserFormValidator
    ) {
        this.userService = userService;
        this.registerUserFormValidator = registerUserFormValidator;
        this.editUserFormValidator = editUserFormValidator;
    }

    @InitBinder("registerUserForm")
    protected void setRegisterUserFormValidator(WebDataBinder binder) {
        binder.setValidator(registerUserFormValidator);
    }

    @InitBinder("editUserForm")
    protected void setEditUserFormValidator(WebDataBinder binder) {
        binder.setValidator(editUserFormValidator);
    }

    @GetMapping("/all-users")
    public Page<UserModel> getAllUsers(Pageable pageable) {
        return userService.findAllUsers(pageable);
    }

    @GetMapping("{userId:\\d+}")
    public UserModel getUser(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/create-user")
    public UserModel createUser(
        @Valid @RequestBody RegisterUserForm form,
        @RequestParam String role
    ) {
        if (StringUtils.isNotBlank(role)) {
            userService.checkIfRoleValid(role);
        }

        return userService.create(form, Role.valueOf(role.toUpperCase()));
    }

    @PutMapping("/{userId:\\d+}/edit")
    public UserModel editUser(
        @PathVariable Long userId,
        @Valid @RequestBody EditUserForm form,
        @RequestParam(required = false) String role
    ) {
        userService.checkIfRoleValid(role);

        return userService.editUser(
            userId,
            form,
            Role.valueOf(role.toUpperCase())
        );
    }

    @DeleteMapping("/{userId:\\d+}/delete")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return String.format("User with id:%s is successfully deleted", userId);
    }
}
