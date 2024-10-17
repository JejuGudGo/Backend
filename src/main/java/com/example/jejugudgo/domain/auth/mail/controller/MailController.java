package com.example.jejugudgo.domain.auth.mail.controller;

import com.example.jejugudgo.domain.auth.mail.dto.EmailAuthenticationRequest;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.auth.mail.dto.MailAuthenticationRequest;
import com.example.jejugudgo.domain.auth.mail.service.MailSendService;
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
    private final MailSendService mailSendService;

    @PostMapping("/mail/send")
    public ResponseEntity<?> sendAuthenticationEmail(@RequestBody EmailRequest request) throws MessagingException {
        MailAuthenticationRequest mailAuthenticationMessage = new MailAuthenticationRequest(
                request.email(),
                "[제주걷GO] 이메일 인증 코드입니다."
        );

        mailSendService.sendAuthCode(mailAuthenticationMessage);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/mail/check")
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody EmailAuthenticationRequest request) {
        return ResponseEntity.ok().build();
    }
}
