package com.pictspace.back.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.pictspace.back.entities.BookingStatus;  // Add this import

import lombok.Data;  // Add this import
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BookingRequestDto {
    private Long id;
    private Long userId;
    private Long roomName;
    private Long timeslotId; // ✅ Change from departmentId to timeslotId
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private String audience;
    private BookingStatus status; 
}
