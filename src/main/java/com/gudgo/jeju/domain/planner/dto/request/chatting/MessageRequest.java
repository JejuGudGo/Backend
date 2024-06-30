package com.gudgo.jeju.domain.planner.dto.request.chatting;

import java.util.List;
import java.util.Map;

public record MessageRequest (
        Long userId,
        String message,
        List<Map<String, String>> images
) {
}