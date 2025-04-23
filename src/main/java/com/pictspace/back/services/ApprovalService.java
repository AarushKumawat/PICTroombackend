package com.pictspace.back.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pictspace.back.entities.BookingRequest;
import com.pictspace.back.entities.BookingStatus;  // ✅ Import the Enum
import com.pictspace.back.entities.Timeslot;
import com.pictspace.back.entities.TimeslotStatus;
import com.pictspace.back.repositories.BookingRequestRepository;
import com.pictspace.back.repositories.TimeslotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final BookingRequestRepository bookingRequestRepository;
    private final TimeslotRepository timeslotRepository;
    // private final UserRepository userRepository;

    @Transactional
    public String approveBooking(Long bookingId, Long approverId) {
        BookingRequest bookingRequest = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));
    
        // ✅ Compare approverId with the ID of approvedBy user
        if (!bookingRequest.getApprovedBy().getId().equals(approverId)) {
            throw new RuntimeException("You are not authorized to approve this request");
        }
    
        // ✅ Approve the booking using Enum
        bookingRequest.setStatus(BookingStatus.APPROVED);
        bookingRequestRepository.save(bookingRequest);
    
        // ✅ Update timeslot status
        Timeslot timeslot = bookingRequest.getTimeslot();
        timeslot.setStatus(TimeslotStatus.BOOKED);
        timeslotRepository.save(timeslot);
    
        return "Booking approved!";
    }
    

    @Transactional
    public String rejectBooking(Long bookingId, Long approverId) {
        BookingRequest bookingRequest = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        // Check if approver is correct
        if (!bookingRequest.getApprovedBy().getId().equals(approverId)) {
            throw new RuntimeException("You are not authorized to reject this request");
        }

        // ✅ Reject the booking using Enum
        bookingRequest.setStatus(BookingStatus.REJECTED);
        bookingRequestRepository.save(bookingRequest);

        return "Booking rejected.";
    }
}
