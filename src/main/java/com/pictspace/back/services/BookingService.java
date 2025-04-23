package com.pictspace.back.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pictspace.back.dto.ApprovalRequestDto;
import com.pictspace.back.dto.BookingRequestDto;
import com.pictspace.back.dto.BookingResponseDto;
import com.pictspace.back.entities.BookingRequest;
import com.pictspace.back.entities.BookingStatus;
import com.pictspace.back.entities.Notification;
import com.pictspace.back.entities.Role;
import com.pictspace.back.entities.Room;
import com.pictspace.back.entities.Timeslot;
import com.pictspace.back.entities.TimeslotStatus;
import com.pictspace.back.entities.User;
import com.pictspace.back.repositories.BookingRequestRepository;
import com.pictspace.back.repositories.NotificationRepository;
import com.pictspace.back.repositories.RoomRepository;
import com.pictspace.back.repositories.TimeslotRepository;
import com.pictspace.back.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRequestRepository bookingRequestRepository;
    private final TimeslotRepository timeslotRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public List<Room> getAvailableRooms(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return roomRepository.findAvailableRooms(date, startTime, endTime);
    }
    /**
     * Creates a booking request and updates the timeslot status.
     */
    public String createBooking(BookingRequestDto bookingDto, boolean requiresApproval) {
        // ✅ Find user
        User user = userRepository.findById(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Find timeslot
        Timeslot timeslot = timeslotRepository.findById(bookingDto.getTimeslotId())
                .orElseThrow(() -> new RuntimeException("Timeslot not found"));

        // ✅ Create a new Booking Request
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUser(user);
        bookingRequest.setRoom(timeslot.getRoom());
        bookingRequest.setTimeslot(timeslot);
        bookingRequest.setReason(bookingDto.getReason());
        bookingRequest.setAudience(bookingDto.getAudience());

        // ✅ Set approval logic
        bookingRequest.setRequiresApproval(requiresApproval);
        bookingRequest.setStatus(requiresApproval ? BookingStatus.PENDING : BookingStatus.APPROVED);

        bookingRequestRepository.save(bookingRequest);

        // ✅ Update timeslot if approval is not needed
        if (!requiresApproval) {
            timeslot.setStatus(TimeslotStatus.BOOKED);
            timeslotRepository.save(timeslot);
        }

        return "Booking request submitted successfully";
    }

    /**
     * Approve or reject a booking request.
     */
    @Transactional
    public String handleBookingApproval(ApprovalRequestDto approvalDto) {
        BookingRequest booking = bookingRequestRepository.findById(approvalDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking request not found"));
    
        User approver = userRepository.findById(approvalDto.getApproverId())
                .orElseThrow(() -> new RuntimeException("Approver not found"));
    
        // ✅ Use Enum comparison for Role
        if (!(approver.getRole() == Role.HOD || approver.getRole() == Role.Principal)) {
            throw new RuntimeException("Unauthorized: Only HODs or Principal can approve/reject bookings.");
        }
    
        // ✅ Check if approver is assigned
        if (booking.getApprovedBy() == null || !booking.getApprovedBy().getId().equals(approver.getId())) {
            throw new RuntimeException("Unauthorized: You are not assigned as the approver for this booking.");
        }
    
        if (approvalDto.isApproved()) {
            booking.setStatus(BookingStatus.APPROVED);  // ✅ Use Enum, not String
            booking.getTimeslot().setStatus(TimeslotStatus.BOOKED);
            booking.setApprovedBy(approver);
            bookingRequestRepository.save(booking);
            timeslotRepository.save(booking.getTimeslot());
    
            sendNotification(booking.getUser().getId(), "Your booking request has been APPROVED.");
            return "Booking approved successfully.";
        } else {
            booking.setStatus(BookingStatus.REJECTED);  // ✅ Use Enum, not String
            booking.getTimeslot().setStatus(TimeslotStatus.AVAILABLE);
            bookingRequestRepository.save(booking);
            timeslotRepository.save(booking.getTimeslot());
    
            sendNotification(booking.getUser().getId(), "Your booking request has been REJECTED. Reason: " + approvalDto.getMessage());
            return "Booking rejected.";
        }
    }
    
    /**
     * Scheduled task to release expired bookings every 5 minutes.
     */
    @Scheduled(fixedRate = 300000) // Runs every 5 minutes
    @Transactional
    public void releaseExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Timeslot> expiredTimeslots = timeslotRepository.findByStatusAndEndTimeBefore(TimeslotStatus.BOOKED, now);

        for (Timeslot timeslot : expiredTimeslots) {
            timeslot.setStatus(TimeslotStatus.AVAILABLE);
        }

        timeslotRepository.saveAll(expiredTimeslots);
        System.out.println("Released expired bookings: " + expiredTimeslots.size());
    }

    /**
     * Send notifications to users.
     */
    private void sendNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUser(userRepository.findById(userId).orElseThrow());
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<BookingResponseDto> getPendingRequests() {
        List<BookingRequest> requests = bookingRequestRepository.findByStatus(BookingStatus.PENDING);
        return convertToDto(requests);
    }

    public List<BookingResponseDto> getApprovedRequests() {
        List<BookingRequest> requests = bookingRequestRepository.findByStatus(BookingStatus.APPROVED);
        return convertToDto(requests);
    }

    public List<BookingResponseDto> getUserRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        List<BookingRequest> requests = bookingRequestRepository.findByUserId(user.getId());
        return convertToDto(requests);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {
        List<BookingRequest> bookings = bookingRequestRepository.findAll();
        return convertToDto(bookings);
    }

    private List<BookingResponseDto> convertToDto(List<BookingRequest> requests) {
        return requests.stream()
            .map(request -> BookingResponseDto.builder()
                .id(request.getId())
                .roomId(request.getRoom().getId())
                .roomName(request.getRoom().getName())
                .timeslotId(request.getTimeslot().getId())
                .date(request.getTimeslot().getDate())
                .startTime(request.getTimeslot().getStartTime())
                .endTime(request.getTimeslot().getEndTime())
                .requestedBy(request.getUser().getUsername())
                .reason(request.getReason())
                .audience(request.getAudience())
                .status(request.getStatus())
                .requiresApproval(request.isRequiresApproval())
                .userId(request.getUser().getId())
                .build())
            .collect(Collectors.toList());
    }

}
