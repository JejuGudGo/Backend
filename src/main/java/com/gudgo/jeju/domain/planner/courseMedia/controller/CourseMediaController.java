package com.gudgo.jeju.domain.planner.courseMedia.controller;


import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.service.CourseMediaService;
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

    /* GET: 특정 코스의 모든 미디어 가져오기 */
    @GetMapping(value = "/{userId}/planners/{plannerId}/course/medias")
    public ResponseEntity<List<CourseMediaResponseDto>> getMedias(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId
    ) {
        List<CourseMediaResponseDto> courseMedias = courseMediaService.getMedias(plannerId);
        return ResponseEntity.ok(courseMedias);
    }

    /* GET: 특정 코스 미디어 상세 정보 가져오기 */
    @GetMapping(value = "/{userId}/planners/{plannerId}/course/medias/{mediaId}")
    public ResponseEntity<CourseMediaResponseDto> get(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("mediaId") Long mediaId
    ) {
        return ResponseEntity.ok(courseMediaService.getMedia(mediaId));
    }

    /* POST: 코스 미디어 생성  */
    @PostMapping(value="/{userId}/planners/{plannerId}/course/medias")
    public ResponseEntity<?> create(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @RequestPart("request") CourseMediaCreateRequestDto requestDto,
            @RequestPart("image") MultipartFile image
    ) throws Exception {
        courseMediaService.create(userId, plannerId, image, requestDto);
        return ResponseEntity.ok().build();
    }

    /* PATCH: 특정 코스 미디어 정보 업데이트 */
    @PatchMapping(
            value = "/{userId}/planners/{plannerId}/course/medias/{mediaId}")
    public ResponseEntity<?> update(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("mediaId") Long mediaId,
            @RequestPart("image") MultipartFile image,
            @RequestPart("request") @Valid CourseMediaUpdateRequestDto requestDto
    ) throws Exception {
        courseMediaService.update(userId, mediaId, image, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{userId}/planners/{plannerId}/course/medias/{mediaId}")
    public ResponseEntity<?> delete(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("mediaId") Long mediaId
    ) {
        courseMediaService.delete(mediaId);
        return ResponseEntity.ok().build();
    }
}
