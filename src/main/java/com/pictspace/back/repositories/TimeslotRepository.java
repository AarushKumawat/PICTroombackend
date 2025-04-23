package com.pictspace.back.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pictspace.back.entities.Timeslot;
import com.pictspace.back.entities.TimeslotStatus;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    // ✅ Find available timeslots for a specific room and date
    List<Timeslot> findByRoomIdAndDateAndStatus(Long roomId, LocalDate date, TimeslotStatus status);

    // ✅ Find expired bookings (timeslots that should be released)
    List<Timeslot> findByStatusAndEndTimeBefore(TimeslotStatus status, LocalDateTime endTime);

    List<Long> findByDateAndStartTimeAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);
}
