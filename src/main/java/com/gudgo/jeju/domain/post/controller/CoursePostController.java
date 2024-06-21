package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.course.dto.request.participant.ParticipantJoinRequest;
import com.gudgo.jeju.domain.course.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.post.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.query.CoursePostQueryService;
import com.gudgo.jeju.domain.post.service.CoursePostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/posts/courses")
@RequiredArgsConstructor
public class CoursePostController {
    private final CoursePostService coursePostService;
    private final CoursePostQueryService coursePostQueryService;

    /* GET: 모든 걷기 게시글 조회
     * GET /api/v1/posts/courses */
    @GetMapping(value = "" )
    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
        return coursePostQueryService.getCoursePosts(pageable);
    }

    /* GET: 특정 걷기 게시글 조회
     * GET /api/v1/posts/courses/{postId} */
    @GetMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> getCoursePost(@PathVariable("postId") Long postId) {
        CoursePostResponse response = coursePostService.getCoursePost(postId);
        return ResponseEntity.ok(response);
    }

    /* POST: 걷기 게시글 생성
     * POST /api/v1/posts/courses */
    @PostMapping(value = "")
    public ResponseEntity<CoursePostResponse> createCoursePost(@RequestBody CoursePostCreateRequest request) {
        coursePostService.create(request);
        return ResponseEntity.ok().build();
    }

    /* PATCH: 특정 걷기 게시글 정보 업데이트
     * PATCH /api/v1/posts/courses/{postId} */
    @PatchMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> updateCoursePost(@PathVariable("postId") Long postId, @RequestBody CoursePostUpdateRequest request) {
        CoursePostResponse response = coursePostService.update(postId, request);

        return ResponseEntity.ok(response);
    }

    /* DELETE: 특정 걷기 게시글 삭제
     * DELETE /api/v1/posts/courses/{postId} */
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<?> deleteCoursePost(@PathVariable("postId") Long postId) {
        coursePostService.delete(postId);

        return ResponseEntity.ok().build();
    }

    /* POST: 참여 요청 (동행 신청)
     * POST /api/v1/posts/courses/{postId}/participants */
    @PostMapping(value = "/{postId}/participants")
    public ResponseEntity<?> requestJoin(@PathVariable("postId") Long postId, HttpServletRequest request) {
        coursePostService.requestJoin(postId, request);
        return ResponseEntity.ok().build();
    }

    /* POST: 참여 취소 (동행 신청 취소)
     * POST /api/v1/posts/courses/{postId}/participants/cancel */
    @PatchMapping(value = "/{postId}/participants/cancel")
    public ResponseEntity<?> requestCancel(@PathVariable("postId") Long courseId, HttpServletRequest request) {
        coursePostService.requestCancel(courseId, request);
        return ResponseEntity.ok().build();
    }


    /* GET: 특정 걷기 게시글 동행 신청자 목록 조회
     * GET /api/v1/posts/courses/{postId}/participants */
    @GetMapping(value = "/{postId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable("postId") Long courseId) {
        return ResponseEntity.ok(coursePostService.getParticipants(courseId));

    }

    /* GET: 특정 걷기 게시글 동행 승낙 완료자 조회
     * GET /api/v1/posts/courses/{postId}/participants/approved */
    @GetMapping(value="/{postId}/participants/approved")
    public ResponseEntity<List<ParticipantResponse>> getApprovedParticipants(@PathVariable("postId") Long courseId) {
        return ResponseEntity.ok(coursePostService.getApprovedParticipants(courseId));

    }

    /* PATCH: 동행 승낙
     * PATCH /api/v1/posts/courses/{postId}/participants/{userId}/approve */
    @PatchMapping(value = "/{postId}/participants/{userId}/approve")
    public ResponseEntity<?> approveUser(@PathVariable("postId") Long courseId, @PathVariable("userId") Long userId) {
        coursePostService.approveUser(courseId, userId);
        return ResponseEntity.ok().build();
    }

    /* PATCH: 동행 거부
     * PATCH /api/v1/posts/courses/{postId}/participants/{userId}/not-approve */
    @PatchMapping(value = "/{postId}/participants/{userId}/not-approve")
    public ResponseEntity<?> notApproveUser(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        coursePostService.notApproveUser(postId, userId);
        return ResponseEntity.ok().build();
    }

    /* PATCH: 특정 걷기 게시글 모집 마감
     * PATCH /api/v1/posts/courses/finish/{postId}/not-approve */
    @PatchMapping(value = "/finish/{postId}/not-approve")
    public ResponseEntity<?> finishRecruit(@PathVariable("postId") Long postId) {
        coursePostService.finishRecruit(postId);
        return ResponseEntity.ok().build();
    }
}
