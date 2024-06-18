package com.gudgo.jeju.domain.post.dto.request;

public record ColumnPostUpdateRequest(
        Long userId,
        String title,
        String content
) {
}
