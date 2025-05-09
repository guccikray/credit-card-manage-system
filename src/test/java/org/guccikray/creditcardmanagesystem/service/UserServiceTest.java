package org.guccikray.creditcardmanagesystem.service;


import org.guccikray.creditcardmanagesystem.assembler.UserModelAssembler;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.guccikray.creditcardmanagesystem.db.enums.Role;
import org.guccikray.creditcardmanagesystem.exception.InvalidRoleException;
import org.guccikray.creditcardmanagesystem.exception.UserNotFoundException;
import org.guccikray.creditcardmanagesystem.exception.UserWithThisEmailAlreadyExistException;
import org.guccikray.creditcardmanagesystem.exception.WrongPasswordException;
import org.guccikray.creditcardmanagesystem.form.EditUserForm;
import org.guccikray.creditcardmanagesystem.form.RegisterUserForm;
import org.guccikray.creditcardmanagesystem.form.SignInForm;
import org.guccikray.creditcardmanagesystem.model.UserModel;
import org.guccikray.creditcardmanagesystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    UserModelAssembler userModelAssembler;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    private static RegisterUserForm registerUserForm;
    private static SignInForm signInForm;
    private static EditUserForm editUserForm;
    private static User user;
    private static UserModel userModel;

    @BeforeAll
    static void init() {
        registerUserForm = new RegisterUserForm()
            .setName("name")
            .setSurname("surname")
            .setEmail("email")
            .setPassword("pass");

        signInForm = new SignInForm()
            .setEmail("email")
            .setPassword("pass");

        editUserForm = (EditUserForm) new EditUserForm()
            .setName("name")
            .setSurname("surname")
            .setEmail("email")
            .setPassword("pass");

        user = (User) new User()
            .setName("name")
            .setSurname("surname")
            .setEmail("email")
            .setPassword("pass")
            .setRole(Role.ROLE_USER)
            .setId(1L);

        userModel = new UserModel()
            .setUserId(1L)
            .setName("name")
            .setSurname("surname")
            .setEmail("email")
            .setRole(Role.ROLE_USER);
    }


    @Test
    void testCreateUserAndEmailIsTaken() {
        when(userRepository.existsByEmail(registerUserForm.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(registerUserForm, Role.ROLE_USER))
            .isInstanceOf(UserWithThisEmailAlreadyExistException.class)
            .hasMessage("This email is already taken");
    }

    @Test
    void testCreateUserSuccessfully() {
        when(userRepository.existsByEmail(registerUserForm.getEmail())).thenReturn(false);
        userService.create(registerUserForm, Role.ROLE_USER);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignInUserNotExists() {
        when(userRepository.findByEmail(signInForm.getEmail())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.signIn(signInForm))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User not found");
    }

    @Test
    void testSignInUserWrongPassword() {
        when(userRepository.findByEmail(signInForm.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
            eq(signInForm.getPassword()),
            eq(user.getPassword()))
        ).thenReturn(false);
        assertThatThrownBy(() -> userService.signIn(signInForm))
            .isInstanceOf(WrongPasswordException.class)
            .hasMessage("Password is wrong");
    }

    @Test
    void testSignIsSuccessfully() {
        when(userRepository.findByEmail(signInForm.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
            eq(signInForm.getPassword()),
            eq(user.getPassword()))
        ).thenReturn(true);
        assertThat(userService.signIn(signInForm)).isInstanceOf(String.class);
    }

    @Test
    void testGetUserInfoSuccessfully() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(user)).thenReturn(userModel);
        assertEquals(userService.getUserInfo(user.getId()), userModel);
    }

    @Test
    void testEditUserSuccessfully() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(user)).thenReturn(userModel);
        assertEquals(userService.editUser(user.getId(), editUserForm, Role.ROLE_USER), userModel);
    }

    @Test
    void testDeleteUserSuccessfully() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testCheckIfRoleValidFails() {
        assertThatThrownBy(() -> userService.checkIfRoleValid("role_support"))
            .isInstanceOf(InvalidRoleException.class)
            .hasMessage("This type of role does not exist");
    }

}
