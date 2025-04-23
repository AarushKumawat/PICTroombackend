package com.pictspace.back.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import jakarta.persistence.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "booking_requests")
public class BookingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"bookingRequests", "password", "approvals", "handler", "hibernateLazyInitializer"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnoreProperties({"bookingRequests", "timeslots", "handler", "hibernateLazyInitializer"})
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"bookingRequests", "rooms", "users", "handler", "hibernateLazyInitializer"})
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id", nullable = false)
    @JsonIgnoreProperties({"bookingRequests", "room", "handler", "hibernateLazyInitializer"})
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_by", nullable = false)
    private Role requesterRole;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private String audience;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "requires_approval")
    private boolean requiresApproval = false;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @OneToMany(mappedBy = "bookingRequest")
    @JsonIgnore
    private Set<Approval> approvals;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ✅ Getter for Room ID
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    // ✅ Getter for Requested By (User ID)
    public Long getRequestedBy() {
        return user != null ? user.getId() : null;
    }

    // ✅ Getter for Timeslot ID
    public Long getTimeslotId() {
        return timeslot != null ? timeslot.getId() : null;
    }

    public LocalDate getDate() {
        return timeslot != null ? timeslot.getDate() : null;
    }

    public LocalTime getStartTime() {
        return timeslot != null ? timeslot.getStartTime() : null;
    }

    public LocalTime getEndTime() {
        return timeslot != null ? timeslot.getEndTime() : null;
    }
}
