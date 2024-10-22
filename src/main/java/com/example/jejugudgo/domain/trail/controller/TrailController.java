package com.example.jejugudgo.domain.trail.controller;

import com.example.jejugudgo.domain.trail.dto.TrailDetailResponse;
import com.example.jejugudgo.domain.trail.dto.TrailListResponse;
import com.example.jejugudgo.domain.trail.service.TrailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/trail")
@RequiredArgsConstructor
public class TrailController {
    private final TrailService trailService;

    @GetMapping(value = "")
    public ResponseEntity<List<TrailListResponse>> getTrails(@RequestParam("type") String query) {
        List<TrailListResponse> responses = trailService.getTrails(query);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/{trailId}")
    public ResponseEntity<TrailDetailResponse> getTrailDetail(@PathVariable("trailId") Long trailId) {
        TrailDetailResponse response = trailService.getTrail(trailId);
        return ResponseEntity.ok(response);
    }
}
