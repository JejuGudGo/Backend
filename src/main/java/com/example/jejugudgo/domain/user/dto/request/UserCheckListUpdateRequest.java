package com.example.jejugudgo.domain.user.dto.request;

public record UserCheckListUpdateRequest(
        String content,
        boolean isFinished
) {
}
