package com.gudgo.jeju.domain.planner.courseMedia.controller;

import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaBackImagesResponseDto;
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

    @GetMapping("/{userId}/media")
    public ResponseEntity<List<CourseMediaResponseDto>> getMediaList(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(courseMediaService.getAllMedias(userId));
    }

    @GetMapping("/{userId}/media/backImage")
    public ResponseEntity<List<CourseMediaBackImagesResponseDto>> getBackImagesList(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId
    ) {
        return ResponseEntity.ok(courseMediaService.getAllBackImages(userId));
    }

    @GetMapping("/{userId}/media/{mediaId}")
    public ResponseEntity<CourseMediaResponseDto> getMedia(
            @PathVariable("userId") Long userId,
            @PathVariable("mediaId") Long mediaId
    ) {
        return ResponseEntity.ok(courseMediaService.getMedia(userId, mediaId));
    }

    @PostMapping("/{userId}/planners/{plannerId}/media")
    public ResponseEntity<?> createMedia(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @RequestPart("request") @Valid CourseMediaCreateRequestDto requestDto,
            @RequestPart("selfieImage") MultipartFile selfieImage,
            @RequestPart("backImage") MultipartFile backImage
    ) throws Exception {
        courseMediaService.create(userId, plannerId, selfieImage, backImage, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/planners/{plannerId}/media/{mediaId}")
    public ResponseEntity<?> updateMedia(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("mediaId") Long mediaId,
            @RequestPart("request") @Valid CourseMediaUpdateRequestDto requestDto,
            @RequestPart("selfieImage") MultipartFile selfieImage,
            @RequestPart("backImage") MultipartFile backImage
    ) throws Exception {
        courseMediaService.update(userId, plannerId, mediaId, selfieImage, backImage, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/planners/{plannerId}/media/{mediaId}")
    public ResponseEntity<?> deleteMedia(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("mediaId") Long mediaId
    ) {
        courseMediaService.delete(userId, plannerId, mediaId);
        return ResponseEntity.ok().build();
    }
}