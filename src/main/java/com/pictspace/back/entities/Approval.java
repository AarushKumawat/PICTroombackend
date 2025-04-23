package com.pictspace.back.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "approvals")
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnore
    private BookingRequest bookingRequest;

    @ManyToOne
    @JoinColumn(name = "approved_by", nullable = false)
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @PrePersist
    protected void onCreate() {
        approvedAt = LocalDateTime.now();
    }
}
