package com.gudgo.jeju.global.auth.basic.dto.response;

import java.time.LocalDateTime;

public record FindAuthResponseDto(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt
) {
}
