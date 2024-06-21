package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.UserCourseResponseDto;
import com.gudgo.jeju.domain.course.query.UserCourseQueryService;
import com.gudgo.jeju.domain.course.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/course/user")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserCourseController {
    private final CourseService courseService;
    private final UserCourseQueryService userCourseQueryService;


    /* GET: 모든 사용자의 코스 목록 조회
     *  GET /api/v1/course/user/list */
    @GetMapping(value = "/list")
    public Page<UserCourseResponseDto> getCourses(Pageable pageable) {
        return userCourseQueryService.getUserCourses(pageable);
    }

    /* POST: 새로운 코스 생성
    *  POST /api/v1/course/user */
    @PostMapping(value = "")
    public ResponseEntity<?> create(@Valid @RequestBody CourseCreateRequestDto courseCreateRequestDto, HttpServletRequest request) {
        courseService.newCourse(courseCreateRequestDto, request);
        return ResponseEntity.ok().build();
    }


    /* GET : 특정 user가 생성한 코스 목록 조회
    *  GET /api/v1/course/user */
    @GetMapping(value = "")
    public ResponseEntity<List<UserCourseResponseDto>> getCourseListByUser(HttpServletRequest request) {
        return ResponseEntity.ok(courseService.getCourseListByUser(request));
    }


    /* DELETE : 코스 삭제
     * DELETE /api/v1/course/user/{courseId}) */
    @DeleteMapping(value = "/{courseId}")
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
