package org.guccikray.creditcardmanagesystem.service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
import org.guccikray.creditcardmanagesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserModelAssembler userModelAssembler
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userModelAssembler = userModelAssembler;
    }

    @Transactional
    public UserModel create(
        @NotNull RegisterUserForm form,
        @NotNull Role role
    ) {
        boolean existsByEmail = userRepository.existsByEmail(form.getEmail());

        if (existsByEmail) {
            throw new UserWithThisEmailAlreadyExistException("This email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        User user = new User()
            .setName(form.getName())
            .setSurname(form.getSurname())
            .setPassword(encodedPassword)
            .setEmail(form.getEmail())
            .setRole(role);

        // несмотря на то, что метод обёрнут в транзакцию, сущность не прикреплена к
        // контексту, т.к. это новый объект и поэтому нужно явно вызывать метод save()
        userRepository.save(user);
        return userModelAssembler.toModel(user);
    }

    /**
     * Метод для авторизации пользователя
     * @param form форма с имейлом и паролем
     * @return jwt токен с помощью которого пользователь будет отправлять запросы
     */
    public String signIn(@NotNull SignInForm form) {
        User user = findByEmail(form.getEmail());

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Password is wrong");
        }

        return JwtUtil.generateToken(user.getId());
    }

    public UserModel getUserInfo(@NotNull Long userId) {
        return userModelAssembler.toModel(findById(userId));
    }

    @Transactional
    public UserModel editUser(
            @NotNull Long userId,
            @NotNull EditUserForm form,
            @Nullable Role role
    ) {
        User user = findById(userId);

        if (Objects.nonNull(role) && user.getRole() != role) {
            user.setRole(role);
        }

        String name = Objects.isNull(form.getName()) ? user.getName() : form.getName();
        String surname = Objects.isNull(form.getSurname()) ? user.getSurname() : form.getSurname();
        String email = Objects.isNull(form.getEmail()) ? user.getEmail() : form.getEmail();
        String password;

        if (Objects.nonNull(form.getPassword())) {
            password = passwordEncoder.encode(form.getPassword());
        } else {
            password = user.getPassword();
        }

        user.setName(name)
            .setSurname(surname)
            .setEmail(email)
            .setPassword(password);

        return userModelAssembler.toModel(user);
    }

    public Page<UserModel> findAllUsers(@NotNull Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return userModelAssembler.toPagedModel(users);
    }

    @Transactional
    public void deleteUser(@NotNull Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public void checkIfRoleValid(@NotNull String role) {
        if (!Role.valid(role)) {
            throw new InvalidRoleException("This type of role does not exist");
        }
    }

    private User findByEmail(@NotNull String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return optionalUser.get();
    }

    private User findById(@NotNull Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return optionalUser.get();
    }

}
