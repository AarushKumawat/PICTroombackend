package com.pictspace.back.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pictspace.back.entities.User;
import com.pictspace.back.repositories.UserRepository;
import com.pictspace.back.services.ApprovalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/approvals")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;
    private final UserRepository userRepository; // ✅ Inject UserRepository

    @PostMapping("/approve/{bookingId}")
    public ResponseEntity<String> approveBooking(@PathVariable Long bookingId, Authentication authentication) {
        // ✅ Fetch approver ID from the database
        User approver = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Approver not found"));

        Long approverId = approver.getId();

        return ResponseEntity.ok(approvalService.approveBooking(bookingId, approverId));
    }

    @PostMapping("/reject/{bookingId}")
    public ResponseEntity<String> rejectBooking(@PathVariable Long bookingId, Authentication authentication) {
        // ✅ Fetch approver ID from the database
        User approver = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Approver not found"));

        Long approverId = approver.getId();

        return ResponseEntity.ok(approvalService.rejectBooking(bookingId, approverId));
    }
}
