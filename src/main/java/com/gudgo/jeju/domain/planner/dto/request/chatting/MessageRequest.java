package com.gudgo.jeju.domain.planner.dto.request.chatting;

public record MessageRequest (
        Long userId,
        String message
) {
}
