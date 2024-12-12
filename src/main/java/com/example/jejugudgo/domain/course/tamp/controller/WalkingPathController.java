package com.example.jejugudgo.domain.course.tamp.controller;

import com.example.jejugudgo.domain.course.tamp.dto.request.TMapRequest;
import com.example.jejugudgo.domain.course.tamp.dto.request.WalkingPathRequest;
import com.example.jejugudgo.domain.course.tamp.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.course.tamp.service.TMapRequestService;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/course/user/walk")
@RequiredArgsConstructor
public class WalkingPathController {
    private final TMapRequestService tMapRequestService;
    private final ApiResponseUtil apiResponseUtil;

//    @GetMapping
//    public ResponseEntity<CommonApiResponse> getLine(HttpServletRequest httpRequest, @RequestBody WalkingPathRequest walkingPathRequest) {
//        WalkingPathResponse response = tMapRequestService.getLine(httpRequest, walkingPathRequest);
//        return ResponseEntity.ok(apiResponseUtil.success(response));
//    }

    // 테스트용
    @PostMapping
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest httpRequest, @RequestBody SpotInfoRequest spotInfoRequest) {
        WalkingPathResponse response = tMapRequestService.create(spotInfoRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}
