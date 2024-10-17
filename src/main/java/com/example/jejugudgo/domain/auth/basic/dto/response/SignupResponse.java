package com.example.jejugudgo.domain.auth.basic.dto.response;

public record SignupResponse(
        Long userId,
        String name,
        String nickname
) {
}
