package com.gudgo.jeju.domain.post.column.dto.request;

public record CommentCreateRequest(
        Long userId,
        String content
) {
}
