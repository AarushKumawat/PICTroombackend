package com.pictspace.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        
        // Configure serialization features
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Write dates as ISO-8601
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // Do not fail on empty beans
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // Pretty print JSON output
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true); // Use enum toString for serialization
        objectMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true); // Avoid infinite recursion
        
        return objectMapper;
    }
}