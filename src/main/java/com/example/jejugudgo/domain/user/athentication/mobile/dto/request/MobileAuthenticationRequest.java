package com.example.jejugudgo.domain.user.athentication.mobile.dto.request;

public record MobileAuthenticationRequest(
        String name,
        String phoneNumber,
        String authCode
) {
}
