package com.example.jejugudgo.domain.auth.basic.dto.request;

public record PasswordUpdateRequest(
        String email,
        String password
) {
}
