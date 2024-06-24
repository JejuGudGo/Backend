package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.course.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.course.service.CourseMediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@RestController
public class CourseMediaController {
    private final CourseMediaService courseMediaService;

    /* GET: 특정 코스의 모든 미디어 가져오기
     * GET /api/v1/course/media?courseId={courseId} */
    @GetMapping(value = "/{userId}/courses/{courseId}/medias")
    public ResponseEntity<List<CourseMediaResponseDto>> getMedias(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long CourseId
    ) {
        List<CourseMediaResponseDto> courseMedias = courseMediaService.getMedias(CourseId);
        return ResponseEntity.ok(courseMedias);
    }

    /* GET: 특정 코스 미디어 상세 정보 가져오기
     * GET /api/v1/course/media/detail?id={id} */
    @GetMapping(value = "/{userId}/courses/{courseId}/medias/{mediaId}")
    public ResponseEntity<CourseMediaResponseDto> get(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @PathVariable("mediaId") Long mediaId
    ) {
        return ResponseEntity.ok(courseMediaService.getMedia(mediaId));
    }

    /* POST: 스팟 기록 생성
     * POST /api/v1/course/media */
    @PostMapping(value="/{userId}/courses/{courseId}/medias")
    public ResponseEntity<?> create(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @RequestPart("request") CourseMediaCreateRequestDto requestDto,
            @RequestPart("image") MultipartFile image
    ) throws Exception {
        courseMediaService.create(userId, courseId, image, requestDto);
        return ResponseEntity.ok().build();
    }

    /* PATCH: 특정 코스 미디어 정보 업데이트
     * PATCH /api/v1/course/media/{id} */
    @PatchMapping(value = "/{userId}/courses/{courseId}/medias/{mediaId}")
    public ResponseEntity<?> update(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @PathVariable("mediaId") Long mediaId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("request") @Valid @RequestBody CourseMediaUpdateRequestDto requestDto
    ) throws Exception {
        courseMediaService.update(userId, mediaId, image, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{userId}/courses/{courseId}/medias/{mediaId}")
    public ResponseEntity<?> delete(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @PathVariable("mediaId") Long mediaId
    ) {
        courseMediaService.delete(mediaId);
        return ResponseEntity.ok().build();
    }
}
