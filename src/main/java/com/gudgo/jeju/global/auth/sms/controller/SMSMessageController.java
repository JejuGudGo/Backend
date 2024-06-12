package com.gudgo.jeju.global.auth.sms.controller;


import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationDTO;
import com.gudgo.jeju.global.auth.sms.service.SMSMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/sms")
@RestController
public class SMSMessageController {
    private final SMSMessageService smsMessageService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> sendSMSForverification(@RequestBody @Valid SMSMessageDTO smsMessageDTO) throws Exception {
        smsMessageService.getSMSVerificationBeforeSignup(smsMessageDTO);
        return ResponseEntity.ok(smsMessageDTO);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> checkUserUsingVerificationCode(@RequestBody SMSVerificationDTO smsVerificationDTO) throws Exception{
        smsMessageService.checkUsersUsingVerificationCode(smsVerificationDTO.getPhoneNumber()
        ,smsVerificationDTO.getVerificationCode());

        return ResponseEntity.ok(smsVerificationDTO);

    }
}
