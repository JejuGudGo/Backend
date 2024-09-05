package com.gudgo.jeju.global.auth.basic.dto.request;

public record FindAuthByEmailRequestDto(
        String findEmail,
        String name,
        String authEmail
) {
}
