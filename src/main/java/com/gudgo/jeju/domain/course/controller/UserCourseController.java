package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.course.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserCourseController {
    private final CourseService courseService;

    /* POST: 새로운 코스 생성
    *  POST /api/v1/course/user */
    @PostMapping(value = "/user")
    public ResponseEntity<?> create(@Valid @RequestBody CourseCreateRequestDto courseCreateRequestDto, HttpServletRequest request) {
        courseService.newCourse(courseCreateRequestDto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{courseId}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    /* GET: 모든 사용자의 코스 목록 조회
    *  GET /api/v1/course/users */
    @GetMapping(value = "/users")
    public ResponseEntity<List<CourseResponseDto>> getCourseList() {
        return ResponseEntity.ok(courseService.getCourseList());
    }


    /* GET : 특정 user가 생성한 코스 목록 조회
    *  GET /api/v1/course/user */
    @GetMapping(value = "/user")
    public ResponseEntity<List<CourseResponseDto>> getCourseListByUser(HttpServletRequest request) {
        return ResponseEntity.ok(courseService.getCourseListByUser(request));
    }


    /* DELETE : 코스 삭제
     * DELETE /api/v1/course/user/{courseId}) */
    @DeleteMapping(value = "/user/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    /* PATCH : 코스 수정
     * PATCH : /api/v1/course/update?courseId={courseId} */
//    @PatchMapping(value = "/update")
//    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @Valid @RequestBody UpdateCourseRequestDto updateCourseRequestDto) {
//        courseService.updateCourse(courseId, updateCourseRequestDto);
//        return ResponseEntity.ok().build();
//    }

}
