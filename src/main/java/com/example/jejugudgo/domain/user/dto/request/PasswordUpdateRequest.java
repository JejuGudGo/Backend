package com.example.jejugudgo.domain.user.dto.request;

public record PasswordUpdateRequest(
        String email,
        String password
) {
}
