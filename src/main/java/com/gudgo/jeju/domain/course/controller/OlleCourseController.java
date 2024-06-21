package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.response.OlleCourseResponseDto;
import com.gudgo.jeju.domain.course.query.OlleCourseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/course/olle")
@RequiredArgsConstructor
@Slf4j
@RestController
public class OlleCourseController {

    private final OlleCourseQueryService olleCourseQueryService;
    /* GET: 올레 코스 전체 목록 조회 */
    @GetMapping(value = "")
    public Page<OlleCourseResponseDto> getOllecourses(Pageable pageable) {
        return olleCourseQueryService.getOlleCourses(pageable);
    }
}
