package com.gudgo.jeju.global.auth.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SMSVerificationDTO {
    private String name;
    private String phoneNumber;
    private String verificationCode;
}
