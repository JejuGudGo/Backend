package com.gudgo.jeju.domain.planner.course.controller;


import com.gudgo.jeju.domain.planner.course.dto.request.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.query.CourseQueryService;
import com.gudgo.jeju.domain.planner.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;
import retrofit2.http.POST;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class CourseController {
    private final CourseService courseService;
    private final CourseQueryService courseQueryService;

    @PatchMapping(value = "/users/{userId}/planners/{plannerId}/course", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateMyCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("title") String title,
            @RequestPart("content") String content
    ) throws Exception {
        CourseUpdateRequestDto requestDto = new CourseUpdateRequestDto(title, content);
        courseService.updateCourse(userId, plannerId, file, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/course/{courseId}")
    public ResponseEntity<?> startCourse(@PathVariable("courseId") Long courseId) {
        courseService.updateCourseStartAt(courseId);
        return ResponseEntity.ok().build();
    }
}