package com.example.jejugudgo.domain.user.account.dto.request;

public record PasswordUpdateRequest(
        String email,
        String password
) {
}
