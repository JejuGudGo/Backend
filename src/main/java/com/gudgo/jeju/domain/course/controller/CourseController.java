package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateByOtherCourseRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.course.query.CourseQueryService;
import com.gudgo.jeju.domain.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class CourseController {
    private final CourseService courseService;
    private final CourseQueryService courseQueryService;

    /* GET : 특정 user가 생성한 코스 목록 조회
     *  GET /api/v1/course/user */
    @GetMapping(value = "/users/{userId}/courses")
    public Page<CourseResponseDto> getMyCourses(@PathVariable("userId") Long userId, Pageable pageable) {
        return courseQueryService.getMyCourses(userId, pageable);
    }

    @GetMapping(value = "/users/{userId}/courses/user")
    public Page<CourseResponseDto> getUserCourses(@PathVariable("userId") Long userId, Pageable pageable) {
        return courseQueryService.getUserCourses(pageable);
    }

    @GetMapping(value = "/users/{userId}/courses/{courseId}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    /* POST: 새로운 코스 생성
     *  POST /api/v1/course/user */
    @PostMapping(value = "/users/{userId}/courses")
    public ResponseEntity<?> create(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody CourseCreateRequestDto courseCreateRequestDto
    ) {
        courseService.create(userId, courseCreateRequestDto);
        return ResponseEntity.ok().build();
    }

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

    /* PATCH : 코스 수정
     * PATCH : /api/v1/course/update?courseId={courseId} */
    @PatchMapping(value = "/users/{userId}/courses/{courseId}")
    public ResponseEntity<?> updateCourse(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody CourseUpdateRequestDto courseUpdateRequestDto
    ) {
        courseService.update(courseId, courseUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /* DELETE : 코스 삭제
     * DELETE /api/v1/course/user/{courseId}) */
    @DeleteMapping(value = "/users/{userId}/courses/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable("userId") Long userId, @PathVariable("courseId") Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok().build();
    }
}