package com.gudgo.jeju.domain.planner.controller;

import com.gudgo.jeju.domain.planner.dto.request.course.CourseCreateByOtherCourseRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PlannerController {

    /* POST: 걷기 계획 생성 (유저 코스 퍼오기)
     *  POST: /api/v1/plan/user-course   */
    @PostMapping(value = "/users/{userId}/courses/user/{courseId}")
    public ResponseEntity<?> createByUserCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody CourseCreateByOtherCourseRequestDto request)
    {
        courseService.createByUserCourse(userId, courseId, request);
        return ResponseEntity.ok().build();
    }

    /* POST: 걷기 계획 생성 (올레 코스 퍼오기)
     *  POST: /api/v1/plan/olle-course   */
    @PostMapping(value = "/users/{userId}/courses/olle/{courseId}")
    public ResponseEntity<?> createCourseByOlleCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody CourseCreateByOtherCourseRequestDto request)
    {
        courseService.createCourseByOlleCourse(userId, courseId, request);
        return ResponseEntity.ok().build();
    }
}
