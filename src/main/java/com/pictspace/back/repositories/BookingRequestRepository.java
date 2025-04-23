package com.pictspace.back.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pictspace.back.entities.BookingRequest;
import com.pictspace.back.entities.BookingStatus;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
     @Query("SELECT br FROM BookingRequest br WHERE br.timeslot.date = :date")
     List<BookingRequest> findByDate(@Param("date") LocalDate date);
     // New methods
     List<BookingRequest> findByStatus(BookingStatus status);
     List<BookingRequest> findByUserId(Long userId);
}
