package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.post.dto.request.ColumnPostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.ColumnPostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.ColumnPostResponse;
import com.gudgo.jeju.domain.post.query.ColumnPostQueryService;
import com.gudgo.jeju.domain.post.service.ColumnPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/posts/columns")
@RequiredArgsConstructor
public class ColumnPostController {
    private final ColumnPostService columnPostService;
    private final ColumnPostQueryService columnPostQueryService;

    @GetMapping(value = "" )
    public Page<ColumnPostResponse> getColumnPosts(Pageable pageable) {
        return columnPostQueryService.getColums(pageable);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<ColumnPostResponse> getColumnPost(@PathVariable("postId") Long postId) {
        ColumnPostResponse columnPostResponse = columnPostService.get(postId);

        return ResponseEntity.ok(columnPostResponse);
    }

    @PostMapping(value = "")
    public ResponseEntity<ColumnPostResponse> createColumnPost(
            @RequestPart("request") ColumnPostCreateRequest request,
            @RequestPart("image") MultipartFile[] images) throws Exception {

        ColumnPostResponse response = columnPostService.create(request, images);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{postId}")
    public ResponseEntity<?> updateColumnPost(
            @PathVariable("postId") Long postId, @RequestBody ColumnPostUpdateRequest request) {
        ColumnPostResponse response = columnPostService.update(postId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<?> deleteCoursePost(@PathVariable("postId") Long postId) {
        columnPostService.delete(postId);

        return ResponseEntity.ok().build();
    }
}
