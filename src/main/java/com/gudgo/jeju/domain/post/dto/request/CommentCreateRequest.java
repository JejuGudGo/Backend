package com.gudgo.jeju.domain.post.dto.request;

public record CommentCreateRequest(
        Long userId,
        String content
) {
}
