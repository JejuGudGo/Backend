package com.gudgo.jeju.global.auth.sms.service;


import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import com.gudgo.jeju.global.util.RedisUtil;
import com.gudgo.jeju.global.util.SMSMessageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SMSMessageService {
    private final SMSMessageUtil smsMessageUtil;
    private final RedisUtil redisUtil;

    public void getSMSVerificationBeforeSignup(SMSMessageDTO smsMessageDTO) {
        smsMessageUtil.verificationUser(smsMessageDTO);

        String verificationCode = smsMessageUtil.generateVerificationCode();
        String phoneNumber = smsMessageDTO.phoneNumber();

        smsMessageUtil.sendMessage(phoneNumber, verificationCode);
        smsMessageUtil.saveDataForCheckUser(phoneNumber, verificationCode);
    }
    public void checkUsersUsingVerificationCode(String phoneNumber, String verificationCode) throws BadRequestException {
        String redisValue = redisUtil.getData(phoneNumber);
        if (!redisValue.equals(verificationCode)) {
            throw new BadRequestException();
        }
    }
}
