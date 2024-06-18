package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.course.service.SpotService;
import jakarta.validation.Valid;
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


    /* POST: 새로운 스팟 생성
     * POST /api/v1/spot */
    @PostMapping(value = "/spot")
    public ResponseEntity<?> newSpot(@Valid @RequestBody SpotCreateRequestDto spotCreateRequestDto) {
        spotService.newSpot(spotCreateRequestDto);
        return ResponseEntity.ok().build();
    }


    /* GET: courseId가 동일한 것들을 리스트로 반환(코스 순서 기준으로 오름차순 정렬)
     * GET /api/v1/course/spots?courseId={courseId} */
    @GetMapping(value = "/course/spots")
    public ResponseEntity<List<SpotResponseDto>> getSpotsByCourseId(@RequestParam Long courseId) {
        List<SpotResponseDto> spots = spotService.getSpotsByCourseId(courseId);
        return ResponseEntity.ok(spots);

    }

    /* GET: id값으로 특정 스팟 조회
     * GET /api/v1/spot?courseId={id} */
    @GetMapping(value ="/spot")
    public  ResponseEntity<SpotResponseDto> getSpot(@RequestParam Long id) {
        return ResponseEntity.ok(spotService.getSpot(id));
    }

    /* PATCH: id값으로 특정 스팟 완료 처리
     * PATCH /api/v1/spot/{id}/complete */
    @PatchMapping(value = "/spot/{id}/complete")
    public ResponseEntity<?> completeSpot(@PathVariable Long id) {
        spotService.completedSpot(id);
        return ResponseEntity.ok().build();
    }

    /* PATCH: id값으로 특정 스팟의 코스 선택 횟수 증가
     * PATCH /api/v1/spot/{id}/increase-count */
    @PatchMapping("/spot/{id}/increase-count")
    public ResponseEntity<?> increaseCount(@PathVariable Long id) {
        spotService.increaseCount(id);
        return ResponseEntity.ok().build();
    }

    // 삭제
    /* DELETE: id값으로 특정 스팟 삭제
    *  DELETE /api/vi/spot/{id}*/
    @DeleteMapping(value = "/spot/{id}")
    public ResponseEntity<?> deleteSpot(@PathVariable Long id) {
        spotService.deleteSpot(id);
        return ResponseEntity.ok().build();
    }
}
