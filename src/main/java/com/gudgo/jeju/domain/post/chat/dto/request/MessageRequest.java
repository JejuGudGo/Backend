package com.gudgo.jeju.domain.post.chat.dto.request;

import java.util.List;
import java.util.Map;

public record MessageRequest (
        Long userId,
        String message,
        List<Map<String, String>> images
) {
}