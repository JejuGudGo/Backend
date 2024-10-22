package com.example.jejugudgo.domain.event.controller;

import com.example.jejugudgo.domain.event.dto.EventListResponse;
import com.example.jejugudgo.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/events")
public class EventController {
    private final EventService eventService;

    @GetMapping(value = "")
    public ResponseEntity<List<EventListResponse>> getEvents(@RequestParam("status") String status) {
        List<EventListResponse> events = eventService.getEvents(status);
        return ResponseEntity.ok(events);
    }
}
