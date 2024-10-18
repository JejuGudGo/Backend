package com.example.jejugudgo.domain.auth.mobile.service;

import com.example.jejugudgo.domain.auth.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.auth.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileAuthenticationService {
    private final SMSMessageService smsMessageService;
    private final RedisUtil redisUtil;

    public void sendAuthCode(MobilAuthCodeRequest request) {
        String authCode = smsMessageService.generateAuthenticationCode();
        String phoneNumber = request.phoneNumber();

        smsMessageService.sendMessage(phoneNumber, authCode);
        smsMessageService.saveDataForCheckUser(phoneNumber, authCode);
    }

    public void checkAuthCode(MobileAuthenticationRequest request) throws BadRequestException {
        String redisValue = redisUtil.getData(request.phoneNumber());
        if (!redisValue.equals(request.authCode())) {
            throw new BadRequestException("인증번호가 불일치 합니다.");
        }
    }
}
