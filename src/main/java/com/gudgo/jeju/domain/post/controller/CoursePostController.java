package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.course.dto.request.participant.ParticipantJoinRequest;
import com.gudgo.jeju.domain.course.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.post.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
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

//    @GetMapping(value = "" )
//    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
//        return
//    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> getCoursePost(@PathVariable("postId") Long postId) {
        CoursePostResponse response = coursePostService.getCoursePost(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "")
    public ResponseEntity<CoursePostResponse> createCoursePost(@RequestBody CoursePostCreateRequest request) {
        coursePostService.create(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> updateCoursePost(@PathVariable("postId") Long postId, @RequestBody CoursePostUpdateRequest request) {
        CoursePostResponse response = coursePostService.update(postId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<?> deleteCoursePost(@PathVariable("postId") Long postId) {
        coursePostService.delete(postId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{postId}/participants")
    public ResponseEntity<?> requestJoin(@PathVariable("postId") Long postId, HttpServletRequest request) {
        coursePostService.requestJoin(postId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{postId}/participants/cancel")
    public ResponseEntity<?> requestCancel(@PathVariable("postId") Long courseId, HttpServletRequest request) {
        coursePostService.requestCancel(courseId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{postId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable("postId") Long courseId) {
        return ResponseEntity.ok(coursePostService.getParticipants(courseId));

    }

    @GetMapping(value="{postId}/participants/approved")
    public ResponseEntity<List<ParticipantResponse>> getApprovedParticipants(@PathVariable("postId") Long courseId) {
        return ResponseEntity.ok(coursePostService.getApprovedParticipants(courseId));

    }

    @PatchMapping(value = "/{postId}/participants/{userId}/approve")
    public ResponseEntity<?> approveUser(@PathVariable("postId") Long courseId, @PathVariable("userId") Long userId) {
        coursePostService.approveUser(courseId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{postId}/participants/{userId}/not-approve")
    public ResponseEntity<?> notApproveUser(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        coursePostService.notApproveUser(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/finish/{postId}/not-approve")
    public ResponseEntity<?> finishRecruit(@PathVariable("postId") Long postId) {
        coursePostService.finishRecruit(postId);
        return ResponseEntity.ok().build();
    }
}
