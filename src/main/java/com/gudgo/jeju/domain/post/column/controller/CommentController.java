package com.gudgo.jeju.domain.post.column.controller;

import com.gudgo.jeju.domain.post.column.dto.request.CommentCreateRequest;
import com.gudgo.jeju.domain.post.column.dto.request.CommentUpdateRequest;
import com.gudgo.jeju.domain.post.column.dto.response.CommentResponse;
import com.gudgo.jeju.domain.post.column.dto.response.NestedCommentResponse;
import com.gudgo.jeju.domain.post.column.query.CommentQueryService;
import com.gudgo.jeju.domain.post.column.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentQueryService commentQueryService;
    private final CommentService commentService;

    @GetMapping(value = "/{postId}/comments")
    public Page<CommentResponse> getComments(@PathVariable("postId") Long postId, Pageable pageable) {
        return commentQueryService.getComments(postId, pageable);
    }

    @PostMapping(value = "/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable("postId") Long postId, @RequestBody CommentCreateRequest request) {
        CommentResponse response = commentService.createComment(postId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value ="/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.updateComment(commentId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value ="/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/{postId}/comments/{commentId}/nested")
    public Page<NestedCommentResponse> getNestedComments(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, Pageable pageable) {
        return commentQueryService.getNestedComments(commentId, pageable);
    }

    @PostMapping(value = "/{postId}/comments/{commentId}/nested")
    public ResponseEntity<NestedCommentResponse> createNestedComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestBody CommentCreateRequest request) {
        NestedCommentResponse response = commentService.createNestedComment(postId, commentId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value ="/{postId}/comments/{commentId}/nested/{nestedId}")
    public ResponseEntity<NestedCommentResponse> updateNestedComment(
            @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @PathVariable("nestedId") Long nestedId, @RequestBody CommentUpdateRequest request) {
        NestedCommentResponse response = commentService.updateNestedComment(nestedId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value ="/{postId}/comments/{commentId}/nested/{nestedId}")
    public ResponseEntity<?> deleteNestedComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @PathVariable("nestedId") Long nestedId) {
        commentService.deleteNestedComment(nestedId);

        return ResponseEntity.ok().build();
    }
}
