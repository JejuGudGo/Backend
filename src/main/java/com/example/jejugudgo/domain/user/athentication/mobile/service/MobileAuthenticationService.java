package com.example.jejugudgo.domain.user.athentication.mobile.service;

import com.example.jejugudgo.domain.user.athentication.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.user.athentication.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.domain.user.athentication.validation.AuthenticationCodeRequest;
import com.example.jejugudgo.domain.user.athentication.validation.AuthenticationValidator;
import com.example.jejugudgo.domain.user.common.validation.ValidationManager;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileAuthenticationService {
    private final SMSMessageService smsMessageService;
    private final ValidationManager validationManager;
    private final AuthenticationValidator authenticationValidator;

    public void sendAuthenticationCode(MobilAuthCodeRequest request) {
        String phoneNumber = request.phoneNumber();

        // 전화번호 형식 검증
        validationManager.validatePhoneNumPattern(phoneNumber);
        // 인증번호 생성
        String authCode = smsMessageService.generateAuthenticationCode();
        // SMS 발송
        SingleMessageSentResponse response = smsMessageService.sendMessage(phoneNumber, authCode);

        // 잔액 부족 에러 체크
        if (response.getStatusCode().equals("1030")) {
            throw new CustomException(RetCode.RET_CODE05);
        }

        smsMessageService.setAuthentcationCode(phoneNumber, authCode);
    }

    public void validationAuthenticationCode(MobileAuthenticationRequest request) {
        AuthenticationCodeRequest authRequest = new AuthenticationCodeRequest(
                request.phoneNumber(),
                request.authCode()
        );

        authenticationValidator.isAuthenticationCodeValid(authRequest);
    }
}
