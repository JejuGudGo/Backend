package com.gudgo.jeju.domain.planner.dto.response;

import java.util.List;

public record MessageResponse (
        Long userId,
        String nickname,
        Long numberTag,
        String profileImageUrl,
        String message,
        List<String> images
){
}
