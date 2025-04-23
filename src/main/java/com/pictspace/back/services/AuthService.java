package com.pictspace.back.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pictspace.back.entities.User;
import com.pictspace.back.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password, String role) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if the role matches
            if (user.getRole().name().equalsIgnoreCase(role)) {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }
}
