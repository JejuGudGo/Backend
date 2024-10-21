package com.example.jejugudgo.domain.user.dto.response;

public record UserCheckListResponse(
        Long checkItemId,
        Long userId,
        String content,
        boolean isFinished
) {
}
