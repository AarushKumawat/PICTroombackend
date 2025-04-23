package com.pictspace.back.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pictspace.back.entities.ChatResponse;
import com.pictspace.back.entities.ChatbotProperties;
import com.pictspace.back.entities.HuggingFaceProperties;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final HuggingFaceProperties huggingFaceProperties;
    private final ChatbotProperties chatbotProperties;
    private List<String> conversationHistory = new ArrayList<>();
    private final Random random = new Random();
    private final Map<String, List<String>> responseMap = new HashMap<>();
    
    @Autowired
    public ChatbotService(
            RestTemplate restTemplate, 
            HuggingFaceProperties huggingFaceProperties,
            ChatbotProperties chatbotProperties) {
        this.restTemplate = restTemplate;
        this.huggingFaceProperties = huggingFaceProperties;
        this.chatbotProperties = chatbotProperties;
        
        // Initialize fallback responses
        initializeResponses();
        
        // Log initialization
        System.out.println("ChatbotService initialized with URL: " + huggingFaceProperties.getUrl());
    }

    private void initializeResponses() {
        // Greetings
        List<String> greetings = new ArrayList<>();
        greetings.add("Hello! How can I help you with your calendar today?");
        greetings.add("Hi there! I'm your calendar assistant. What can I do for you?");
        greetings.add("Welcome! How can I assist with your scheduling needs?");
        responseMap.put("greeting", greetings);
        
        // Calendar related
        List<String> calendar = new ArrayList<>();
        calendar.add("I can help you manage your calendar. Would you like to view, add, or modify an event?");
        calendar.add("Your calendar is my specialty. I can check availability, add events, or remind you of upcoming appointments.");
        calendar.add("I can assist with all your calendar needs. What specific calendar operation would you like help with?");
        responseMap.put("calendar", calendar);
        
        // Events
        List<String> events = new ArrayList<>();
        events.add("To schedule an event, please provide the date, time, and title.");
        events.add("I'd be happy to add an event to your calendar. What's the name, date, and time for this event?");
        events.add("Creating a new event is easy. Just tell me when it starts, ends, and what it's called.");
        responseMap.put("event", events);
        
        // Booking
        List<String> booking = new ArrayList<>();
        booking.add("I'd be happy to help you book a reservation. What date and time are you looking for?");
        booking.add("Let's book that for you. When would you like to schedule it?");
        booking.add("I can help with booking. Please provide the details like date, time, and duration.");
        responseMap.put("booking", booking);
        
        // Time and date
        List<String> time = new ArrayList<>();
        time.add("I can check your availability or schedule events at specific times. What would you like to know?");
        time.add("I can help you find the perfect time slot. What day are you considering?");
        time.add("Time management is important. I can show you free slots in your schedule.");
        responseMap.put("time", time);
        
        // Availability
        List<String> availability = new ArrayList<>();
        availability.add("I can check your availability. Which day would you like me to check?");
        availability.add("Let me check when you're free. Which date range are you interested in?");
        availability.add("I'll help you find some free time in your schedule. What part of the day works best for you?");
        responseMap.put("availability", availability);
        
        // Cancellations
        List<String> cancel = new ArrayList<>();
        cancel.add("I can help you cancel or reschedule events. Which event would you like to modify?");
        cancel.add("Need to cancel something? Just tell me which appointment you'd like to remove.");
        cancel.add("I understand plans change. Which scheduled event would you like to cancel?");
        responseMap.put("cancel", cancel);
        
        // Default responses
        List<String> defaults = new ArrayList<>();
        defaults.add("I'm your calendar assistant. I can help with scheduling, checking availability, and managing events. How can I assist you today?");
        defaults.add("As your PICT Calendar Assistant, I can help you manage your schedule efficiently. What would you like to do?");
        defaults.add("I'm here to make scheduling easier for you. I can create events, check your availability, or send reminders. What do you need?");
        responseMap.put("default", defaults);
    }

    public String getInitialGreeting() {
        return chatbotProperties.getInitialGreeting();
    }

    public ChatResponse generateResponse(String message) {
        System.out.println("Generating response for: " + message);
        
        // Store conversation history
        conversationHistory.add("User: " + message);
        
        try {
            // Try to use AI model first
            String aiResponse = callHuggingFaceApi(message);
            
            // Check if response is valid
            if (aiResponse != null && !aiResponse.isEmpty() && !aiResponse.contains("Hall of Fame") && aiResponse.length() < 500) {
                // Add to conversation history
                conversationHistory.add("Assistant: " + aiResponse);
                return new ChatResponse(aiResponse);
            } else {
                // Fallback to rule-based response if AI response is problematic
                System.out.println("AI response was invalid, using fallback");
                String fallbackResponse = getLocalResponse(message);
                conversationHistory.add("Assistant: " + fallbackResponse);
                return new ChatResponse(fallbackResponse);
            }
        } catch (Exception e) {
            System.err.println("Error in ChatbotService.generateResponse: " + e.getMessage());
            e.printStackTrace();
            
            // Use fallback response on error
            String fallbackResponse = getLocalResponse(message);
            conversationHistory.add("Assistant: " + fallbackResponse);
            return new ChatResponse(fallbackResponse);
        } finally {
            // Limit history size
            if (conversationHistory.size() > 10) {
                conversationHistory = conversationHistory.subList(conversationHistory.size() - 10, conversationHistory.size());
            }
        }
    }
    
    private String callHuggingFaceApi(String message) {
        try {
            // Create headers with API token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            if (huggingFaceProperties.getToken() != null && !huggingFaceProperties.getToken().isEmpty()) {
                headers.set("Authorization", "Bearer " + huggingFaceProperties.getToken());
            }

            // Create simple input for the model
            Map<String, Object> requestBody = new HashMap<>();
            
            // Use a calendar-focused prompt
            String prompt = "You are a helpful calendar assistant. Keep your response focused on calendar management. User: " + message;
            requestBody.put("inputs", prompt);
            
            // Parameters for reasonable output
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("max_length", chatbotProperties.getResponse().getMaxLength());
            parameters.put("temperature", chatbotProperties.getResponse().getTemperature());
            parameters.put("return_full_text", false);
            requestBody.put("parameters", parameters);
            
            // Create the HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Send the request
            System.out.println("Sending request to HuggingFace API: " + huggingFaceProperties.getUrl());
            ResponseEntity<String> response = restTemplate.postForEntity(
                huggingFaceProperties.getUrl(), 
                entity, 
                String.class
            );
            
            System.out.println("Raw response: " + response.getBody());
            
            // Process the response - simple extraction
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                String processed = response.getBody();
                
                // Remove JSON formatting
                if (processed.contains("generated_text")) {
                    processed = processed.replaceAll(".*\"generated_text\"\\s*:\\s*\"", "");
                    processed = processed.replaceAll("\".*", "");
                }
                
                // Unescape quotes and newlines
                processed = processed.replace("\\\"", "\"").replace("\\n", "\n");
                
                return processed;
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error calling HuggingFace API: " + e.getMessage());
            return null;
        }
    }
    
    private String getLocalResponse(String message) {
        message = message.toLowerCase();
        
        // Check for keywords and return appropriate response
        if (containsAny(message, "hello", "hi", "hey", "greetings")) {
            return getRandomResponse("greeting");
        } else if (containsAny(message, "calendar", "schedule", "planner", "agenda")) {
            return getRandomResponse("calendar");
        } else if (containsAny(message, "event", "meeting", "appointment", "session")) {
            return getRandomResponse("event");
        } else if (containsAny(message, "book", "reserve", "schedule a", "set up")) {
            return getRandomResponse("booking");
        } else if (containsAny(message, "time", "date", "when", "day", "month")) {
            return getRandomResponse("time");
        } else if (containsAny(message, "availability", "free", "available", "open", "slot")) {
            return getRandomResponse("availability");
        } else if (containsAny(message, "cancel", "delete", "remove", "reschedule")) {
            return getRandomResponse("cancel");
        } else {
            return getRandomResponse("default");
        }
    }
    
    private String getRandomResponse(String category) {
        List<String> responses = responseMap.getOrDefault(category, responseMap.get("default"));
        return responses.get(random.nextInt(responses.size()));
    }
    
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}