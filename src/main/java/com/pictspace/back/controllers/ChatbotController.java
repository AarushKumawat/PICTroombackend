package com.pictspace.back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pictspace.back.entities.ChatRequest;
import com.pictspace.back.entities.ChatResponse;
import com.pictspace.back.entities.GreetingResponse;
import com.pictspace.back.services.ChatbotService;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/greeting")
    public ResponseEntity<GreetingResponse> getGreeting() {
        System.out.println("Greeting endpoint called");
        String greeting = chatbotService.getInitialGreeting();
        return ResponseEntity.ok(new GreetingResponse(greeting));
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        System.out.println("Chat endpoint called with message: " + request.getMessage());
        ChatResponse response = chatbotService.generateResponse(request.getMessage());
        return ResponseEntity.ok(response);
    }
}