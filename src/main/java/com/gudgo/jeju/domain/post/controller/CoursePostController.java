package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.post.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.service.CoursePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts/courses")
@RequiredArgsConstructor
public class CoursePostController {
    private final CoursePostService coursePostService;

    @GetMapping(value = "" )
    ResponseEntity<?> getCoursePosts() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> getCoursePost(@PathVariable("postId") Long postId) {
        CoursePostResponse response = coursePostService.read(postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "")
    public ResponseEntity<CoursePostResponse> createCoursePost(@RequestBody CoursePostCreateRequest request) {
        CoursePostResponse response = coursePostService.create(request);

        return ResponseEntity.ok(response);
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

//    @PostMapping(value = "/{postId}")
//    public ResponseEntity<?> requestJoin(@PathVariable("postId") Long postId) {
//
//    }

//    @PutMapping(value = "/{postId}/{userId}")
//    public ResponseEntity<?> approveUser(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
//
//    }

//    @DeleteMapping(value = "/{postId}/{userId}")
//    public ResponseEntity<?> deleteAllCoursePosts(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
//
//    }
}
