package com.example.jejugudgo.domain.user.dto.response;

public record UserCheckListResponse(
        Long checkItemId,
        Long userId,
        Long orderNumber,
        String content,
        boolean isFinished
) {
}
