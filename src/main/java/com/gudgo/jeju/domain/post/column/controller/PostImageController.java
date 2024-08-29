package com.gudgo.jeju.domain.post.column.controller;

import com.gudgo.jeju.domain.post.column.dto.response.PostImageResponse;
import com.gudgo.jeju.domain.post.column.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
public class PostImageController {
    private final PostImageService postImageService;

    @GetMapping(value = "/{postId}/images")
    public ResponseEntity<List<PostImageResponse>> getPostImages(@PathVariable("postId") Long postImageId) {
        List<PostImageResponse> response = postImageService.getImages(postImageId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{postId}/images/{postImageId}")
    public ResponseEntity<?> deletePostImage(@PathVariable("postId") Long postId, @PathVariable("postImageId") Long postImageId) throws IOException {
        postImageService.deleteImage(postImageId);

        return ResponseEntity.ok().build();
    }
}
