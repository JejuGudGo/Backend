package com.gudgo.jeju.domain.post.walk.controller;

import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.walk.query.CoursePostQueryService;
import com.gudgo.jeju.domain.post.walk.service.CoursePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/posts")
@RequiredArgsConstructor
public class CoursePostController {
    private final CoursePostService coursePostService;
    private final CoursePostQueryService coursePostQueryService;

    @GetMapping(value = "" )
    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
        return coursePostQueryService.getCoursePosts(pageable);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> getCoursePost(@PathVariable("postId") Long postId) {
        CoursePostResponse response = coursePostService.getCoursePost(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/planners")
    public ResponseEntity<CoursePostResponse> createCoursePostByUsers(@RequestBody CoursePostCreateRequest request) {
        coursePostService.createByUserCourse(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/olle")
    public ResponseEntity<CoursePostResponse> createCoursePostByOlle(@RequestBody CoursePostCreateRequest request) {
        coursePostService.createByOlle(request);
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
}