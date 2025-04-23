package com.pictspace.back;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.pictspace.back.config.JwtConfig;

@TestConfiguration
public class TestConfig {

    @Bean
    public JwtConfig jwtConfig() {
        JwtConfig config = new JwtConfig();
        config.setSecret("test-secret"); // Test-specific secret
        config.setExpiration(86400000); // Test-specific expiration
        config.setHeader("Authorization"); // Test-specific header
        config.setPrefix("Bearer"); // Test-specific prefix
        return config;
    }
} 