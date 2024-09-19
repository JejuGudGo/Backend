package com.gudgo.jeju.domain.post.walk.controller;

import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostDetailResponse;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostListResponse;
import com.gudgo.jeju.domain.post.walk.query.CoursePostQueryService;
import com.gudgo.jeju.domain.post.walk.service.CoursePostService;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class CoursePostController {

    private final CoursePostService coursePostService;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
    private final CoursePostQueryService coursePostQueryService;

    @GetMapping(value = "posts/course" )
    public Page<CoursePostListResponse> getCoursePosts(@RequestParam("query") String query, Pageable pageable) {
        return coursePostQueryService.getAllCoursePosts(query, pageable);
    }

    @GetMapping(value = "posts/course/{postId}")
    public ResponseEntity<CoursePostDetailResponse> getCoursePost(HttpServletRequest request, @PathVariable("postId") Long postId) {
        Long userId = getUserId(request);
        CoursePostDetailResponse response = coursePostService.getCoursePost(userId, postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/posts/course")
    public ResponseEntity<CoursePostDetailResponse> createCoursePost(@PathVariable Long userId, @RequestBody CoursePostCreateRequest request) {
        CoursePostDetailResponse response = coursePostService.createCoursePost(userId, request);
        return ResponseEntity.ok(response);
    }

    private Long getUserId(HttpServletRequest request) {
        String accessToken = tokenExtractor.getAccessTokenFromHeader(request);
        Long userId = subjectExtractor.getUserIdFromToken(accessToken);
        return userId;
    }
}
