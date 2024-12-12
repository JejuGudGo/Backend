package com.example.jejugudgo.domain.mygudgo.course.controller;

import com.example.jejugudgo.domain.mygudgo.course.dto.request.UserCourseCreateRequest;
import com.example.jejugudgo.domain.mygudgo.course.service.UserCourseService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/course/user")
@RequiredArgsConstructor
public class UserCourseController {

    private final ApiResponseUtil apiResponseUtil;
    private final UserCourseService userCourseService;

    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest httpRequest, UserCourseCreateRequest userCourseCreateRequest) {
        userCourseService.create(httpRequest, userCourseCreateRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

}
