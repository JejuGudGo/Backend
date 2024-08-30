package com.gudgo.jeju.global.auth.sms.controller;


import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationResultResponse;
import com.gudgo.jeju.global.auth.sms.service.SMSMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/sms")
@RestController
public class SMSMessageController {
    private final SMSMessageService smsMessageService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> sendSMSForVerification(@RequestBody @Valid SMSMessageDTO smsMessageDTO) throws Exception {
        smsMessageService.getSMSVerificationBeforeSignup(smsMessageDTO);
        return ResponseEntity.ok(smsMessageDTO);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> checkUserUsingVerificationCode(@RequestBody SMSVerificationDTO smsVerificationDTO) throws Exception{
        smsMessageService.checkUsersUsingVerificationCode(smsVerificationDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/check/later")
    public ResponseEntity<SMSVerificationResultResponse> checkUserUsingVerificationCodeAfterSignup(@RequestBody SMSVerificationDTO smsVerificationDTO) throws Exception{
        SMSVerificationResultResponse response = smsMessageService.checkUsersUsingVerificationCode(smsVerificationDTO);

        return ResponseEntity.ok(response);
    }
}