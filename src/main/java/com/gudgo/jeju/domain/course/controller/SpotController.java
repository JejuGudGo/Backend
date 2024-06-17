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
    public ResponseEntity<?> spot(@Valid @RequestBody SpotCreateRequestDto spotCreateRequestDto) {
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
     * GET /api/v1/course/spot?courseId={id} */
    @GetMapping(value ="/course/spot")
    public  ResponseEntity<SpotResponseDto> getSpot(@RequestParam Long id) {
        return ResponseEntity.ok(spotService.getSpot(id));
    }

    // 수정

    // 삭제
}
