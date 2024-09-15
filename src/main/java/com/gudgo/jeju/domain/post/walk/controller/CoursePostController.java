package com.gudgo.jeju.domain.post.walk.controller;

import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostDetailResponse;
import com.gudgo.jeju.domain.post.walk.service.CoursePostService;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class CoursePostController {

    private final CoursePostService coursePostService;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
//    private final CoursePostQueryService coursePostQueryService;
//
//    @GetMapping(value = "" )
//    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
//        return coursePostQueryService.getCoursePosts(pageable);
//    }
//
    @GetMapping(value = "posts/walk/{postId}")
    public ResponseEntity<CoursePostDetailResponse> getCoursePost(HttpServletRequest request, @PathVariable("postId") Long postId) {
        Long userId = getUserId(request);
        CoursePostDetailResponse response = coursePostService.getCoursePost(userId, postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/posts/walk")
    public ResponseEntity<CoursePostDetailResponse> createCoursePost(@PathVariable Long userId, @RequestBody CoursePostCreateRequest request) {
        CoursePostDetailResponse response = coursePostService.createCoursePost(userId, request);
        return ResponseEntity.ok(response);
    }

//    @PatchMapping(value = "/{postId}")
//    public ResponseEntity<CoursePostResponse> updateCoursePost(@PathVariable("postId") Long postId, @RequestBody CoursePostUpdateRequest request) {
//        CoursePostResponse response = coursePostService.update(postId, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping(value = "/{postId}")
//    public ResponseEntity<?> deleteCoursePost(@PathVariable("postId") Long postId) {
//        coursePostService.delete(postId);
//        return ResponseEntity.ok().build();
//    }

    private Long getUserId(HttpServletRequest request) {
        String accessToken = tokenExtractor.getAccessTokenFromHeader(request);
        Long userId = subjectExtractor.getUserIdFromToken(accessToken);
        return userId;
    }
}
