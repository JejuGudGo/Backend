package com.example.jejugudgo.domain.auth.basic.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
