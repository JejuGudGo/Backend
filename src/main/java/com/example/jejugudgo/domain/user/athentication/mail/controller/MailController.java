package com.example.jejugudgo.domain.user.athentication.mail.controller;

import com.example.jejugudgo.domain.user.athentication.mail.dto.EmailAuthenticationRequest;
import com.example.jejugudgo.domain.user.athentication.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.user.athentication.mail.validation.MailAuthenticationValidator;
import com.example.jejugudgo.domain.user.athentication.mail.service.MailSendService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
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
    private final MailAuthenticationValidator mailAuthenticationValidator;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping("/mail/send")
    public ResponseEntity<CommonApiResponse> sendAuthenticationEmail(@RequestBody EmailRequest request) {
        mailSendService.sendAuthenticationCode(request.email());
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @PostMapping(value = "/mail/check")
    public ResponseEntity<CommonApiResponse> checkAuthenticationCode(@RequestBody EmailAuthenticationRequest request) {
        mailAuthenticationValidator.validateAuthenticationCode(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
