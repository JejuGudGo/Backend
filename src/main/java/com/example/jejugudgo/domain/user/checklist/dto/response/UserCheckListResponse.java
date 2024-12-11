package com.example.jejugudgo.domain.user.checklist.dto.response;

public record UserCheckListResponse(
        Long checkItemId,
        Long userId,
        Long order,
        String content,
        boolean isFinished
) {
}
