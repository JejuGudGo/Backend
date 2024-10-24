package com.example.jejugudgo.domain.auth.mail.service;

import com.example.jejugudgo.domain.auth.mail.dto.EmailAuthenticationRequest;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailAuthService {
    private final RedisUtil redisUtil;

    public void validateAuthCode(EmailAuthenticationRequest request) {
        // Redis에서 저장된 인증번호 조회
        String redisValue = redisUtil.getData(request.email());

        // 인증번호의 형식이 올바르지 않은 경우
        if (!isValidAuthCodeFormat(request.authCode())) {
            throw new CustomException(RetCode.RET_CODE01);
        }

        // 인증번호가 일치하지 않는 경우
        if (!redisValue.equals(request.authCode())) {
            throw new CustomException(RetCode.RET_CODE02);
        }

    }
    // 인증번호의 형식이 유효한지 확인
    private boolean isValidAuthCodeFormat(String authCode) {
        return authCode.matches("\\d{6}");
    }
}
