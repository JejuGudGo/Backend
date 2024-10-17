package com.example.jejugudgo.domain.user.dto.response;

import java.time.LocalDateTime;

public record FindEmailResponse(
        String email,
        LocalDateTime createdAt
) {
}
