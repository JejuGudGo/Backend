package com.gudgo.jeju.domain.post.controller;

import com.gudgo.jeju.domain.post.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
public class PostImageController {
    private final PostImageService postImageService;

    @GetMapping(value = "/{postId}/images")
    public ResponseEntity<?> getPostImages(@PathVariable("postId") Long postImageId) {
        postImageService.getImages(postImageId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{postId}/images/{postImageId}")
    public ResponseEntity<?> deletePostImage(@PathVariable("postId") Long postId, @PathVariable("postImageId") Long postImageId) throws IOException {
        postImageService.deleteImage(postImageId);

        return ResponseEntity.ok().build();
    }
}