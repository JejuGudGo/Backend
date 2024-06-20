package com.gudgo.jeju.domain.post.dto.request;

public record ColumnPostCreateRequest (
        Long userId,
        String title,
        String content
) {
}
