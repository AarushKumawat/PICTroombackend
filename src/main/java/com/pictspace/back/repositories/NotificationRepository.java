package com.pictspace.back.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pictspace.back.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
