package com.pictspace.back.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pictspace.back.dto.BookingRequestDto;
import com.pictspace.back.dto.BookingResponseDto;
import com.pictspace.back.entities.BookingRequest;
import com.pictspace.back.services.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }   

    @PostMapping("/request")
    public ResponseEntity<String> requestBooking(@RequestBody BookingRequest bookingRequest) {
// Convert BookingRequest to BookingRequestDto
        BookingRequestDto bookingDto = new BookingRequestDto();
        bookingDto.setRoomId(bookingRequest.getRoomId());
        bookingDto.setUserId(bookingRequest.getRequestedBy());
        bookingDto.setTimeslotId(bookingRequest.getTimeslotId()); // ✅ FIXED
        bookingDto.setDate(bookingRequest.getDate());
        bookingDto.setStartTime(bookingRequest.getStartTime());
        bookingDto.setEndTime(bookingRequest.getEndTime());
        bookingDto.setReason(bookingRequest.getReason());
        bookingDto.setAudience(bookingRequest.getAudience());

        boolean requiresApproval = bookingRequest.isRequiresApproval(); // ✅ Fetch approval requirement

        // Call createBooking() with the correct DTO and requiresApproval flag
        return ResponseEntity.ok(bookingService.createBooking(bookingDto, requiresApproval)); // ✅ FIXED
    }

    // New endpoints
    @GetMapping("/calendar/admin/requests/pending")
    @PreAuthorize("hasAnyAuthority('HOD', 'Principal')")
    public ResponseEntity<List<BookingResponseDto>> getPendingRequests() {
        List<BookingResponseDto> pendingRequests = bookingService.getPendingRequests();
        System.out.println("Pending Requests Response: " + pendingRequests);
        return ResponseEntity.ok(pendingRequests);
    }

    @GetMapping("/calendar/admin/requests/approved")
    @PreAuthorize("hasAnyAuthority('HOD', 'Principal')")  // Changed from hasAnyRole to hasAnyAuthority
    public ResponseEntity<List<BookingResponseDto>> getApprovedRequests() {
        return ResponseEntity.ok(bookingService.getApprovedRequests());
    }

    @GetMapping("/calendar/requests")
    public ResponseEntity<List<BookingResponseDto>> getUserRequests() {
        return ResponseEntity.ok(bookingService.getUserRequests());
    }

    // @GetMapping("/calendar/requests")
    // public ResponseEntity<List<BookingResponseDto>> getUserBookingRequests() {
    // return ResponseEntity.ok(bookingService.getUserRequests());
    // }



   
}
