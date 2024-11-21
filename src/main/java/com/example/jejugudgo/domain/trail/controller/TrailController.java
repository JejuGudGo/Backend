package com.example.jejugudgo.domain.trail.controller;

import com.example.jejugudgo.domain.trail.dto.TrailDetailResponse;
import com.example.jejugudgo.domain.trail.dto.TrailListResponse;
import com.example.jejugudgo.domain.trail.service.TrailService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/trails")
@RequiredArgsConstructor
public class TrailController {
    private final TrailService trailService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping(value = "/{trailId}")
    public ResponseEntity<CommonApiResponse> getTrailDetail(HttpServletRequest request, @PathVariable("trailId") Long trailId) {
        TrailDetailResponse response = trailService.getTrail(request, trailId);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}
