package com.pictspace.back.controllers;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pictspace.back.entities.Room;
import com.pictspace.back.repositories.RoomRepository;
import com.pictspace.back.repositories.TimeslotRepository;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final TimeslotRepository timeslotRepository;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/available")
    public List<Map<String, Object>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        // Get IDs of rooms that have available timeslots for the given parameters
        List<Long> availableRoomIds = timeslotRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
        
        // Query rooms by these IDs and map to simplified response
        return roomRepository.findAllById(availableRoomIds).stream()
                .map(room -> Map.<String, Object>of(
                "id", (Object) room.getId(),
                "name", (Object) room.getName()
        ))
                .collect(Collectors.toList());

        }
}
