package com.example.jejugudgo.domain.auth.mail.dto;

public record EmailAuthenticationRequest(
        String email,
        String authCode,
        // TODO: 회원가입 유무 어떻게 관리할건지 결정하기
        String status
) {
}
