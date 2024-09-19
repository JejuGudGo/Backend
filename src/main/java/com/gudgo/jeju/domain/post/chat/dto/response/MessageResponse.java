package com.gudgo.jeju.domain.post.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MessageResponse (
        Long userId,
        String nickname,
        Long numberTag,
        String profileImageUrl,
        String message,
        LocalDateTime createdAt,
        List<String> images
){
}
