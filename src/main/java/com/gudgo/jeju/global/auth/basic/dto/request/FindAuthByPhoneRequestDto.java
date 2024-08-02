package com.gudgo.jeju.global.auth.basic.dto.request;

public record FindAuthByPhoneRequestDto(
        String name,
        String phoneNumber
) {
}
