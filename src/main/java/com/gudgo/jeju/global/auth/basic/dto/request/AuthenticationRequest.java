package com.gudgo.jeju.global.auth.basic.dto.request;

public record AuthenticationRequest(
        String email,
        String authCode
) {
}
