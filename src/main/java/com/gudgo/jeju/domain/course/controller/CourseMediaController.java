package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.course.service.CourseMediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/course/media")
@RequiredArgsConstructor
@Slf4j
@RestController
public class CourseMediaController {

    private final CourseMediaService courseMediaService;

    /* POST: 스팟 기록 생성
     * POST /api/v1/course/media */
    @PostMapping(value="")
    public ResponseEntity<?> create(CourseMediaCreateRequestDto requestDto) {
        courseMediaService.newCourseMedia(requestDto);
        return ResponseEntity.ok().build();
    }

    /* GET: 특정 코스의 모든 미디어 가져오기
     * GET /api/v1/course/media?courseId={courseId} */
    @GetMapping(value = "")
    public ResponseEntity<List<CourseMediaResponseDto>> getCourseMediasByCourseId(@RequestParam Long CourseId) {
        List<CourseMediaResponseDto> courseMedias = courseMediaService.getCourseMediasByCourseId(CourseId);
        return ResponseEntity.ok(courseMedias);
    }

    /* GET: 특정 코스 미디어 상세 정보 가져오기
     * GET /api/v1/course/media/detail?id={id} */
    @GetMapping(value = "/detail")
    public ResponseEntity<CourseMediaResponseDto> getCourseMedia(@RequestParam Long id) {
        return ResponseEntity.ok(courseMediaService.getCourseMedia(id));
    }

    /* PATCH: 특정 코스 미디어 정보 업데이트
     * PATCH /api/v1/course/media/{id} */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CourseMediaUpdateRequestDto requestDto) {
        courseMediaService.updateCourseMedia(id, requestDto);
        return ResponseEntity.ok().build();
    }
    }
