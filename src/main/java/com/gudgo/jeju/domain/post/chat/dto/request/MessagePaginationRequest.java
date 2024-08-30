package com.gudgo.jeju.domain.post.chat.dto.request;

public record MessagePaginationRequest(
        int page,
        int size
) {
}
