package com.minjournal.min_journal_backend.services;

import com.minjournal.min_journal_backend.exceptions.InvalidCredentialsException;
import com.minjournal.min_journal_backend.exceptions.UsernameAlreadyExistsException;
import com.minjournal.min_journal_backend.models.User;
import com.minjournal.min_journal_backend.repositories.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new UsernameAlreadyExistsException("That username is already taken");
        }
        String passwordHash = passwordEncoder.encode(password);
        User newUser = new User(username, passwordHash);
        userRepository.save(newUser);
    }

    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPasswordHash())) {
            throw new InvalidCredentialsException("Wrong username or password");
        }
        return user.get();
    }

}
