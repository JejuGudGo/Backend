package com.example.jejugudgo.domain.user.account.dto.request;

public record SignUpRequest(
        TermAgreement termAgreement,
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
