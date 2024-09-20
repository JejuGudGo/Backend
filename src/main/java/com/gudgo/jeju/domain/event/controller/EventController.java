package com.gudgo.jeju.domain.event.controller;

import com.gudgo.jeju.domain.event.dto.response.EventResponse;
import com.gudgo.jeju.domain.event.entity.EventType;
import com.gudgo.jeju.domain.event.query.EventQueryService;
import com.gudgo.jeju.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Slf4j
@RestController
public class EventController {

    private final EventQueryService eventQueryService;
    private final EventService eventService;

    @GetMapping("")
    public Page<EventResponse> getEvents(@RequestParam("query") EventType type, Pageable pageable) {
        return eventQueryService.getEvents(type, pageable);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }
}



