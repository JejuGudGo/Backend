package com.example.jejugudgo.domain.auth.mobile.service;

import com.example.jejugudgo.domain.auth.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.auth.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.domain.auth.validation.PhoneValidation;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileAuthenticationService {
    private final SMSMessageService smsMessageService;
    private final RedisUtil redisUtil;
    private final PhoneValidation phoneValidation;

    public void sendAuthCode(MobilAuthCodeRequest request) {
        String phoneNumber = request.phoneNumber();

        // 전화번호 형식 검증
        phoneValidation.validatePhoneNumber(phoneNumber);

        // 인증번호 생성
        String authCode = smsMessageService.generateAuthenticationCode();

        // SMS 발송
        SingleMessageSentResponse response = smsMessageService.sendMessage(phoneNumber, authCode);

        // 잔액 부족 에러 체크
        if (response.getStatusCode().equals("1030")) {
            throw new CustomException(RetCode.RET_CODE04);
        }

        smsMessageService.saveDataForCheckUser(phoneNumber, authCode);
    }

    public void checkAuthCode(MobileAuthenticationRequest request) {
        String redisValue = redisUtil.getData(request.phoneNumber());

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
