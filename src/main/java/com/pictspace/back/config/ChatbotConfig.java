package com.pictspace.back.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.pictspace.back.entities.ChatbotProperties;
import com.pictspace.back.entities.HuggingFaceProperties;

@Configuration
@PropertySource("classpath:chatbot.properties")
public class ChatbotConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "huggingface.api")
    public HuggingFaceProperties huggingFaceProperties() {
        return new HuggingFaceProperties();
    }
    
    @Bean
    @ConfigurationProperties(prefix = "chatbot")
    public ChatbotProperties chatbotProperties() {
        return new ChatbotProperties();
    }
}
