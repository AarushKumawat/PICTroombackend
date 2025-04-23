package com.pictspace.back.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pictspace.back.entities.Room;
import com.pictspace.back.repositories.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final RoomRepository roomRepository;

    public List<Room> getAvailableRooms(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return roomRepository.findAvailableRooms(date, startTime, endTime);
    }
} 