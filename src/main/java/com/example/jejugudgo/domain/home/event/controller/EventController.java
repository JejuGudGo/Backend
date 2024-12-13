package com.example.jejugudgo.domain.home.event.controller;

import com.example.jejugudgo.domain.home.event.dto.EventListResponse;
import com.example.jejugudgo.domain.home.event.service.EventService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
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
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping(value = "")
    public ResponseEntity<CommonApiResponse> getEvents(@RequestParam("status") String status) {
        List<EventListResponse> events = eventService.getEvents(status);
        return ResponseEntity.ok(apiResponseUtil.success(events, "events"));
    }
}
