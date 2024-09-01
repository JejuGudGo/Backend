package com.gudgo.jeju.global.auth.oauth.dto;

public record OAuth2SignupRequest(
        String email,
        String profileImage,
        String provider
) {
}
