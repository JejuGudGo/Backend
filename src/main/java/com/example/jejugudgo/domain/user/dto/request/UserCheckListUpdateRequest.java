package com.example.jejugudgo.domain.user.dto.request;

public record UserCheckListUpdateRequest(
        String content,
        Boolean isFinished,
        Long orderNumber
) {
}
