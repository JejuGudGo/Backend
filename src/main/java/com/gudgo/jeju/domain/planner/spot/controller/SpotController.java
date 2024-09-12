package com.gudgo.jeju.domain.planner.spot.controller;


import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateUsingApiRequest;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.spot.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class SpotController {
    private final SpotService spotService;

    /* GET: courseId가 동일한 것들을 리스트로 반환(코스 순서 기준으로 오름차순 정렬) */
    @GetMapping(value = "/user/{userId}/planners/{plannerId}/course/spots")
    public ResponseEntity<List<SpotResponseDto>> getSpots(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        List<SpotResponseDto> spots = spotService.getSpots(plannerId);

        return ResponseEntity.ok(spots);

    }

    /* GET: id값으로 특정 스팟 조회 */
    @GetMapping(value ="/user/{userId}/planners/{plannerId}/course/spots/{spotId}")
    public  ResponseEntity<SpotResponseDto> getSpot(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId, @PathVariable("spotId") Long spotId) {
        return ResponseEntity.ok(spotService.getSpot(spotId));
    }

    /* POST: 새로운 스팟 생성 */
    @PostMapping(value = "/user/{userId}/planners/{plannerId}/course/spots")
    public ResponseEntity<?> createSpotByUser(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId, @Valid @RequestBody SpotCreateRequestDto spotCreateRequestDto) {
        spotService.createUserSpot(plannerId, spotCreateRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/user/{userId}/planners/{plannerId}/course/spots/tour")
    public ResponseEntity<?> createSpotUsingTourApi(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId, @RequestBody SpotCreateUsingApiRequest request) throws IOException {
        spotService.createSpotUsingTourApi(plannerId, request);

        return ResponseEntity.ok().build();
    }

    // 삭제
    /* DELETE: id값으로 특정 스팟 삭제 */
    @DeleteMapping(value = "/userId/{userId}/planners/{plannerId}/course/spots/{spotId}")
    public ResponseEntity<?> deleteSpot(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("spotId") Long spotId
    ) throws IllegalAccessException {
        spotService.delete(userId, plannerId, spotId);

        return ResponseEntity.ok().build();
    }

    // TODO: 스팟 순서 변경

    /* PATCH: id값으로 특정 스팟 완료 처리
     * PATCH /api/v1/spot/{id}/complete */
    @PatchMapping(value = "/user/courses/{courseId}/spots/{spotId}/status")
    public ResponseEntity<?> completeSpot(
            @PathVariable("courseId") Long courseId,
            @PathVariable("spotId") Long spotId
    ){
        spotService.completedSpot(courseId, spotId);

        return ResponseEntity.ok().build();
    }

    /* PATCH: id값으로 특정 스팟의 선택 횟수 증가
     * PATCH /api/v1/spot/{id}/increase-count */
    @PatchMapping("/spots/{spotId}/count")
    public ResponseEntity<?> increaseCount(
            @PathVariable("spotId") Long spotId
    ){
        spotService.increaseCount(spotId);

        return ResponseEntity.ok().build();
    }
}
