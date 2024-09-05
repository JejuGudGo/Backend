package com.gudgo.jeju.global.util.mail.controller;

import com.gudgo.jeju.global.auth.basic.dto.request.AuthenticationRequest;
import com.gudgo.jeju.global.auth.basic.dto.request.EmailRequestDto;
import com.gudgo.jeju.global.auth.basic.dto.response.FindAuthResponseDto;
import com.gudgo.jeju.global.auth.basic.service.FindAuthService;
import com.gudgo.jeju.global.util.mail.dto.request.MailAuthenticationMessage;
import com.gudgo.jeju.global.util.mail.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MailController {

    private final FindAuthService findAuthService;
    private final MailService mailService;

    @PostMapping("/authentication/send")
    public ResponseEntity<?> sendAuthenticationEmail(@RequestBody EmailRequestDto request) throws MessagingException {
        MailAuthenticationMessage mailAuthenticationMessage = new MailAuthenticationMessage(
                request.email(),
                "[제주걷GO] 이메일 인증 코드입니다."
        );

        String authCode = mailService.sendMailAuthenticationCode(mailAuthenticationMessage);
        return ResponseEntity.ok(authCode);
    }

    /* 이메일 인증번호 확인*/
    @PostMapping(value = "/authentication/check")
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody AuthenticationRequest request) {
        findAuthService.validateAuthCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/authentication/check/later")
    public ResponseEntity<FindAuthResponseDto> checkAuthenticationCodeAfterSignup(@RequestBody AuthenticationRequest request) {
        findAuthService.validateAuthCode(request);
        FindAuthResponseDto response = findAuthService.validateAuthCodeAfterSignup(request);

        return ResponseEntity.ok(response);
    }
}
