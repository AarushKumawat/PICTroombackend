package com.pictspace.back.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@PropertySource("classpath:jwt.properties")
@Getter
@Setter
public class JwtConfig {
    private String secret;
    private long expiration;
    private String header;
    private String prefix;
    private String cookieName; // Add this for cookie-based token storage
    private boolean secureCookie; // Should cookies be secure only
} 