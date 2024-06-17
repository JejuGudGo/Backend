package com.gudgo.jeju.global.auth.basic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record SignupRequest(

        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "형식에 맞게 입력해주세요")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^[A-Za-z\\d~!@#$%^&*()_\\-+=\\[\\]{}|\\\\;:'\",.<>?/]{8,20}$")
        // 소문자, 특수문자, 8-20 자리 비밀번호
        String password,
        String nickname,

        @NotBlank(message = "이름을 입력해주세요")
        String name,

        @NotBlank(message = "전화번호를 입력해주세요")
        String phoneNumber
) {}
