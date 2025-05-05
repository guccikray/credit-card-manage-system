package org.guccikray.creditcardmanagesystem.initializer;

import org.guccikray.creditcardmanagesystem.db.enums.Role;
import org.guccikray.creditcardmanagesystem.exception.UserWithThisEmailAlreadyExistException;
import org.guccikray.creditcardmanagesystem.form.RegisterUserForm;
import org.guccikray.creditcardmanagesystem.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    private final UserService userService;

    public AdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAdminUser() {
        RegisterUserForm form = new RegisterUserForm()
            .setName("admin")
            .setSurname("user")
            .setEmail("admin@local.com")
            .setPassword("qwerty123");
        try {
            userService.create(form, Role.ROLE_ADMIN);
            System.out.println("Admin user created");
        } catch (UserWithThisEmailAlreadyExistException ignored) {
            System.out.println("Admin user already exists");
        }
    }
}
