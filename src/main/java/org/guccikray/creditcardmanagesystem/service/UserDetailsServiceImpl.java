package org.guccikray.creditcardmanagesystem.service;

import jakarta.validation.constraints.NotNull;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.guccikray.creditcardmanagesystem.exception.UserNotFoundException;
import org.guccikray.creditcardmanagesystem.model.AuthUser;
import org.guccikray.creditcardmanagesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return new AuthUser(optionalUser.get());
    }

    public UserDetails loadUserByUserId(@NotNull Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return new AuthUser(optionalUser.get());
    }
}
