package com.example.jejugudgo.domain.user.checkList.dto.response;

public record UserCheckListResponse(
        Long checkItemId,
        Long userId,
        Long orderNumber,
        String content,
        boolean isFinished
) {
}
