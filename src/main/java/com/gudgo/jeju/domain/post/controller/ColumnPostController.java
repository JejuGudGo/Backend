package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.post.dto.request.ColumnPostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.ColumnPostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.service.ColumnPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts/columns")
@RequiredArgsConstructor
public class ColumnPostController {
    private final ColumnPostService columnPostService;

    @GetMapping(value = "/{postId}")
    public ResponseEntity<?> getCoursePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "")
    public ResponseEntity<CoursePostResponse> createCoursePost(@RequestBody ColumnPostCreateRequest request) {
        columnPostService.create(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<CoursePostResponse> updateCoursePost(@PathVariable("postId") Long postId, @RequestBody ColumnPostUpdateRequest request) {
        columnPostService.update(postId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<?> deleteCoursePost(@PathVariable("postId") Long postId) {
        columnPostService.delete(postId);

        return ResponseEntity.ok().build();
    }
}
