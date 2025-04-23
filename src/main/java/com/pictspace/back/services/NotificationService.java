package com.pictspace.back.services;

import org.springframework.stereotype.Service;

import com.pictspace.back.entities.Notification;
import com.pictspace.back.entities.User;
import com.pictspace.back.repositories.NotificationRepository;
import com.pictspace.back.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository; // Add UserRepository to fetch User

    public void sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);  // Set the User entity instead of just an ID
        notification.setMessage(message);

        notificationRepository.save(notification);
    }
}
