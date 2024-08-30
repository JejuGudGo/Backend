package com.gudgo.jeju.domain.post.column.dto.request;

public record ColumnPostCreateRequest (
        Long userId,
        String title,
        String content
) {
}
