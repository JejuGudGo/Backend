package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.spot.SpotCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.spot.SpotCreateUsingApiRequest;
import com.gudgo.jeju.domain.course.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.course.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/user/{userId}/courses/")
@RequiredArgsConstructor
@Slf4j
@RestController
public class SpotController {
    private final SpotService spotService;

    /* GET: courseId가 동일한 것들을 리스트로 반환(코스 순서 기준으로 오름차순 정렬)
     * GET /api/v1/spots?courseId={courseId} */
    @GetMapping(value = "/{courseId}/spots")
    public ResponseEntity<List<SpotResponseDto>> getSpots(@PathVariable("courseId") Long courseId) {
        List<SpotResponseDto> spots = spotService.getSpots(courseId);

        return ResponseEntity.ok(spots);

    }

    /* GET: id값으로 특정 스팟 조회
     * GET /api/v1/spot?courseId={id} */
    @GetMapping(value ="/{courseId}/spots/{spotId}")
    public  ResponseEntity<SpotResponseDto> getSpot(@PathVariable("courseId") Long courseId, @PathVariable("spotId") Long spotId) {
        return ResponseEntity.ok(spotService.getSpot(spotId));
    }

    /* POST: 새로운 스팟 생성
     * POST /api/v1/spot */
    @PostMapping(value = "/{courseId}/spots/user")
    public ResponseEntity<?> createSpotByUser(@PathVariable("courseId") Long courseId, @Valid @RequestBody SpotCreateRequestDto spotCreateRequestDto) {
        spotService.createUserSpot(courseId, spotCreateRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{courseId}/spots/tour")
    public ResponseEntity<?> createSpotUsingTourApi(@PathVariable("courseId") Long courseId, @RequestBody SpotCreateUsingApiRequest request) throws IOException {
        spotService.createSpotUsingTourApi(courseId, request);

        return ResponseEntity.ok().build();
    }

    /* PATCH: id값으로 특정 스팟 완료 처리
     * PATCH /api/v1/spot/{id}/complete */
    @PatchMapping(value = "/{courseId}/spots/{spotId}/status")
    public ResponseEntity<?> completeSpot(@PathVariable("courseId") Long courseId, @PathVariable("spotId") Long spotId) {
        spotService.completedSpot(courseId, spotId);

        return ResponseEntity.ok().build();
    }

    /* PATCH: id값으로 특정 스팟의 선택 횟수 증가
     * PATCH /api/v1/spot/{id}/increase-count */
    @PatchMapping("/{courseId}/spots/{spotId}/count")
    public ResponseEntity<?> increaseCount(@PathVariable("courseId") Long courseId, @PathVariable("spotId") Long spotId) {
        spotService.increaseCount(spotId);

        return ResponseEntity.ok().build();
    }

    // 삭제
    /* DELETE: id값으로 특정 스팟 삭제
    *  DELETE /api/vi/spot/{id}*/
    @DeleteMapping(value = "/{courseId}/spots/{spotId}")
    public ResponseEntity<?> deleteSpot(@PathVariable("courseId") Long courseId, @PathVariable("spotId") Long spotId) {
        spotService.delete(spotId);

        return ResponseEntity.ok().build();
    }
}
