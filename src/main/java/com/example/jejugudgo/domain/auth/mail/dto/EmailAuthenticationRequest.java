package com.example.jejugudgo.domain.auth.mail.dto;

public record EmailAuthenticationRequest(
        String email,
        String authCode
) {
}
