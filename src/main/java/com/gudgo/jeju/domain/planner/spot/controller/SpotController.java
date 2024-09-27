package com.gudgo.jeju.domain.planner.spot.controller;


import com.gudgo.jeju.domain.planner.spot.dto.request.SpotTimeLabsUpdateRequest;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotUpdateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.response.LastSpotResponse;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotPositionResponse;
import com.gudgo.jeju.domain.planner.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class SpotController {
    private final SpotService spotService;

    /* GET: courseId가 동일한 것들을 리스트로 반환(코스 순서 기준으로 오름차순 정렬) */
    @GetMapping(value = "/planners/{plannerId}/course/spots")
    public ResponseEntity<List<SpotPositionResponse>> getSpots(@PathVariable("plannerId") Long plannerId) {
        List<SpotPositionResponse> spots = spotService.getSpots(plannerId);

        return ResponseEntity.ok(spots);
    }

    /* PATCH: id값으로 특정 스팟 완료 처리
     * PATCH /api/v1/spot/{id}/complete */
    @PatchMapping(value = "/courses/{courseId}/spots/{spotId}/status")
    public ResponseEntity<LastSpotResponse> validateSpot(
            @PathVariable("courseId") Long courseId,
            @PathVariable("spotId") Long spotId,
            @RequestBody SpotTimeLabsUpdateRequest request
    ){
        LastSpotResponse response = spotService.validateSpot(courseId, spotId, request);

        return ResponseEntity.ok(response);
    }
}
