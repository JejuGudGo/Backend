package com.gudgo.jeju.global.auth.oauth.dto;

public record OAuth2LoginRequset (
        String email,
        String provider
) {
}
