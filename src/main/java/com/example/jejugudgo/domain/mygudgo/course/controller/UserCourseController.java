package com.example.jejugudgo.domain.mygudgo.course.controller;

import com.example.jejugudgo.domain.mygudgo.course.dto.request.IdRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.UserCourseCreateRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.UserCourseUpdateRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.UserCourseCreateResponse;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.UserCourseUpdateResponse;
import com.example.jejugudgo.domain.mygudgo.course.service.UserCourseService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/course/user")
@RequiredArgsConstructor
public class UserCourseController {

    private final ApiResponseUtil apiResponseUtil;
    private final UserCourseService userCourseService;

    @PostMapping("/create")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest httpRequest, @RequestBody UserCourseCreateRequest userCourseCreateRequest) {
        UserCourseCreateResponse response = userCourseService.create(httpRequest, userCourseCreateRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @PostMapping("/update")
    public ResponseEntity<CommonApiResponse> update(
            HttpServletRequest httpRequest,
            @RequestParam(value="id", required=true) Long id,
            @RequestParam(value="title", required = false) String title,
            @RequestParam(value="content", required = false) String content,
            @RequestParam(value = "thumbnailImageUrl", required = false) MultipartFile image) throws Exception {
        UserCourseUpdateRequest updateRequest = new UserCourseUpdateRequest(id, image, content, title);

        UserCourseUpdateResponse response = userCourseService.update(httpRequest, updateRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonApiResponse> delete(HttpServletRequest httpRequest, @RequestBody IdRequest idRequest) {
        userCourseService.delete(httpRequest, idRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }



}
