package com.example.jejugudgo.domain.auth.mail.service;

import com.example.jejugudgo.domain.auth.mail.dto.EmailAuthenticationRequest;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailAuthService {
    private final RedisUtil redisUtil;

    public void validateAuthCode(EmailAuthenticationRequest request) throws BadRequestException {
        String redisValue = redisUtil.getData(request.email());
        if (!redisValue.equals(request.authCode())) {
            throw new BadRequestException("인증번호가 불일치 합니다.");
        }
    }
}
