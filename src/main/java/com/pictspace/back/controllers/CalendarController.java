package com.pictspace.back.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pictspace.back.entities.Timeslot;
import com.pictspace.back.entities.TimeslotStatus;
import com.pictspace.back.repositories.TimeslotRepository;

@RestController
@RequestMapping("/calendar")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CalendarController {

    private final TimeslotRepository timeslotRepository;

    public CalendarController(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    @GetMapping("/availability")
    public List<Timeslot> getAvailableSlots(@RequestParam Long roomId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return timeslotRepository.findByRoomIdAndDateAndStatus(roomId, parsedDate, TimeslotStatus.AVAILABLE);
    }
}
