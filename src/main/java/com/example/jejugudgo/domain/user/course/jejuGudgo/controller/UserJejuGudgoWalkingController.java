package com.example.jejugudgo.domain.user.course.jejuGudgo.controller;


import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseOptionCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseOptionResponse;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseOptionService;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCompletedRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.reseponse.UserJejuGudgoWalkingStartResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.serivce.UserJejuGudgoCourseService;
import com.example.jejugudgo.domain.user.course.jejuGudgo.serivce.UserJejuGudgoCourseSpotService;
import com.example.jejugudgo.domain.user.course.jejuGudgo.serivce.UserJejuGudgoWalkingService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/courses/jejugudgo")
@RequiredArgsConstructor
public class UserJejuGudgoWalkingController {

    private final JejuGudgoCourseOptionService jejuGudgoCourseOptionService;
    private final ApiResponseUtil apiResponseUtil;
    private final UserJejuGudgoCourseSpotService userJejuGudgoCourseSpotService;
    private final UserJejuGudgoCourseService userJejuGudgoCourseService;
    private final UserJejuGudgoWalkingService userJejuGudgoWalkingService;


    /**
     * 걷기 옵션 조회
     *
     * @param courseId - 조회할 코스 ID
     * @return 걷기 옵션 목록
     */
    @GetMapping("/{courseId}/walk/option")
    public ResponseEntity<CommonApiResponse> getWalkingOption(@PathVariable("courseId") Long courseId) {
        List<JejuGudgoCourseOptionResponse> response = jejuGudgoCourseOptionService.getOptions(courseId);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    /**
     * 걷기 시작
     *
     * @param courseId - 시작할 코스 ID
     * @param servletRequest - HttpServletRequest 객체 (헤더에서 사용자 정보 추출)
     * @param createRequest - 걷기 옵션 생성 요청 데이터
     * @return userJejuGudgoCourseId 반환
     */
    @PostMapping("/{courseId}/walk/start")
    public ResponseEntity<CommonApiResponse> startWalking(
            @PathVariable("courseId") Long courseId,
            HttpServletRequest servletRequest,
            @RequestBody JejuGudgoCourseOptionCreateRequest createRequest) {
        UserJejuGudgoWalkingStartResponse response = userJejuGudgoWalkingService.startWalking(courseId, servletRequest, createRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }


    /**
     * 스팟 완료 처리
     *
     * @param courseId - userJejuGudgoCourseId
     * @param spotId - 완료 처리할 스팟 ID
     * @param servletRequest - HttpServletRequest 객체 (사용자 정보 추출용)
     * @param completedRequest - 완료 요청 데이터
     * @return CommonApiResponse - 성공 여부 반환
     */
    @PatchMapping("/{courseId}/spot/{spotId}/complete")
    ResponseEntity<CommonApiResponse> walking(
            @PathVariable("courseId") Long courseId,
            @PathVariable("spotId") Long spotId,
            HttpServletRequest servletRequest,
            @RequestBody UserJejuGudgoCourseCompletedRequest completedRequest) {

        // 서비스 호출: 스팟 완료 처리
        userJejuGudgoCourseSpotService.completedSpot(courseId, spotId, servletRequest, completedRequest);

        // 성공 응답 반환
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}