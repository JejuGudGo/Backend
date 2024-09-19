package com.gudgo.jeju.domain.trail.controller;

import com.gudgo.jeju.domain.trail.dto.response.TrailDetailResponse;
import com.gudgo.jeju.domain.trail.dto.response.TrailListResponse;
import com.gudgo.jeju.domain.trail.entity.TrailType;
import com.gudgo.jeju.domain.trail.query.TrailQueryService;
import com.gudgo.jeju.domain.trail.service.TrailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/trails")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TrailController {
    private final TrailService trailService;
    private final TrailQueryService trailQueryService;

    @GetMapping(value = "")
    public Page<TrailListResponse> getTrails(@RequestParam("query")TrailType trailType, Pageable pageable) {
        return trailQueryService.getTrails(trailType, pageable);
    }

    @GetMapping(value = "/{trailId}")
    public ResponseEntity<TrailDetailResponse> getTrail(@PathVariable Long trailId) {
        TrailDetailResponse response = trailService.getTrail(trailId);

        return ResponseEntity.ok(response);
    }
}
