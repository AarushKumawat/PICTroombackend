package com.pictspace.back.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.pictspace.back.entities.BookingStatus;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
@Builder
public class BookingResponseDto {
    private Long id;
    private String roomName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String requestedBy;
    private String reason;
    private String audience;
    private BookingStatus status;
    private boolean requiresApproval;
    private Long roomId; // Add this
    private Long timeslotId; // Add this
    private Long userId;
} 
