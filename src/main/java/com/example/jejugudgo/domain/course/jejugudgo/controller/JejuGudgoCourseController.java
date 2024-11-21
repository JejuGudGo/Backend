package com.example.jejugudgo.domain.course.jejugudgo.controller;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseUpdateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseUpdateResponse;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseService;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoDeleteService;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoUpdateService;
import com.example.jejugudgo.domain.search.dto.sub.JeujuGudgoCourseInfoResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.serivce.UserJejuGudgoCourseService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/courses/jejugudgo")
@RequiredArgsConstructor
public class JejuGudgoCourseController {

    private final ApiResponseUtil apiResponseUtil;
    private final UserJejuGudgoCourseService userJejuGudgoCourseService;
    private final JejuGudgoUpdateService jejuGudgoUpdateService;
    private final JejuGudgoDeleteService jejuGudgoDeleteService;

    /**
     * 유저 코스 생성
     * @param servletRequest - HttpServletRequest 객체 (헤더에서 사용자 정보 추출)
     * @param createRequest - 유저 코스 생성 요청 데이터
     * @return 생성된 코스 정보 응답
     */
    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest servletRequest, @RequestBody UserJejuGudgoCourseCreateRequest createRequest) {
        JeujuGudgoCourseInfoResponse response = userJejuGudgoCourseService.createUserCourse(servletRequest, createRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }


    /**
     * 유저 코스 수정
     *
     * @param courseId - JejuGudgoCourse ID
     * @param servletRequest - 사용자 정보 추출을 위한 HttpServletRequest
     * @param imageUrl - 수정할 이미지 URL
     * @param title - 수정할 제목
     * @param content - 수정할 내용
     * @return 수정 결과
     */
    @PatchMapping("/{courseId}")
    public ResponseEntity<CommonApiResponse> update(
            @PathVariable("courseId") Long courseId,
            HttpServletRequest servletRequest,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content) throws Exception {
        JejuGudgoCourseUpdateRequest updateRequest = new JejuGudgoCourseUpdateRequest(imageUrl, title, content);
        JejuGudgoCourseUpdateResponse response = jejuGudgoUpdateService.updateUserCourse(courseId, servletRequest, updateRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    /**
     * 유저 코스 삭제
     *
     * @param courseId - 삭제할 JejuGudgoCourse ID
     * @param servletRequest - 사용자 요청 정보
     * @return ResponseEntity<CommonApiResponse> - 삭제 결과 반환
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<CommonApiResponse> delete(
            @PathVariable("courseId") Long courseId,
            HttpServletRequest servletRequest) {
        jejuGudgoDeleteService.deleteJejuGudgoCourse(courseId, servletRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
