package com.pictspace.back.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pictspace.back.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM BookingRequest b WHERE b.status IN ('PENDING', 'APPROVED') AND b.timeslot.date = :date AND ((b.timeslot.startTime < :endTime AND b.timeslot.endTime > :startTime)))")
    List<Room> findAvailableRooms(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
