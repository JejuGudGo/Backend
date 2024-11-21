package com.example.jejugudgo.domain.auth.basic.dto.response;

import java.time.LocalDateTime;

public record FindEmailResponse(
        String email,
        LocalDateTime createdAt
) {
}
