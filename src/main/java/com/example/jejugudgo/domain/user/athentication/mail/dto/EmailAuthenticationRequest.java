package com.example.jejugudgo.domain.user.athentication.mail.dto;

public record EmailAuthenticationRequest(
        String email,
        String authCode
) {
}
