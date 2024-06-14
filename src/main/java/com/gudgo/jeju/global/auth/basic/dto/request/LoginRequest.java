package com.gudgo.jeju.global.auth.basic.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
