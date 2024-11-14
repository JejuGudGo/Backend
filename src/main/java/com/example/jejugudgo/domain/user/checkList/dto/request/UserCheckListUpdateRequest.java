package com.example.jejugudgo.domain.user.checkList.dto.request;

public record UserCheckListUpdateRequest(
        String content,
        Boolean isFinished,
        Long orderNumber
) {
}
