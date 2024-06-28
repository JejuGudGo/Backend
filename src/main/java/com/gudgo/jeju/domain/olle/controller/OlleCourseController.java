package com.gudgo.jeju.domain.olle.controller;


import com.gudgo.jeju.domain.olle.dto.response.OlleCourseDetailResponseDto;
import com.gudgo.jeju.domain.olle.dto.response.OlleCourseResponseDto;
import com.gudgo.jeju.domain.olle.query.OlleCourseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/courses/olle")
@RequiredArgsConstructor
@Slf4j
@RestController
public class OlleCourseController {
    private final OlleCourseQueryService olleCourseQueryService;

    /* GET: 올레 코스 전체 목록 조회 */
    @GetMapping(value = "")
    public Page<OlleCourseResponseDto> getOlleCourses(Pageable pageable) {
        return olleCourseQueryService.getOlleCourses(pageable);
    }

    @GetMapping(value = "/{olleId}")
    public OlleCourseDetailResponseDto getOlleCourse(@PathVariable("olleId") Long olleId) {
        return olleCourseQueryService.getOlleCourse(olleId);
    }

//    올레 전체 코스 어떻게 반환?
//    @GetMapping(value = "/{olleId}/route")


//    /* GET: 모든 사용자의 코스 목록 조회
//     *  GET /api/v1/course/users */
//    @GetMapping(value = "/users")
//    public ResponseEntity<List<CourseResponseDto>> getCourseList() {
//        return ResponseEntity.ok(courseService.getCourseList());
//    }
}
