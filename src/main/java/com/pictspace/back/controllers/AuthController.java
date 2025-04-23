package com.pictspace.back.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pictspace.back.dto.LoginRequest;
import com.pictspace.back.entities.User;
import com.pictspace.back.repositories.UserRepository;
import com.pictspace.back.services.AuthService;
import com.pictspace.back.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")  // Enable CORS for the frontend
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.getRole()
        );

        if (isAuthenticated) {
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getAuthorities()
            );

            // Create a HashMap instead of using Map.of() to handle null values
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("role", user.getRole());
            userMap.put("departmentId", user.getDepartment() != null ? user.getDepartment().getId() : null);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userMap);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole(),
                "departmentId", user.getDepartment() != null ? user.getDepartment().getId() : null
        );

        return ResponseEntity.ok(response);
    }
}
