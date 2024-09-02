package com.gudgo.jeju.global.auth.sms.service;


import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationResultResponse;
import com.gudgo.jeju.global.util.RedisUtil;
import com.gudgo.jeju.global.util.SMSMessageUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SMSMessageService {
    private final SMSMessageUtil smsMessageUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;


    public void getAuthCodeBeforeSignup(SMSMessageDTO smsMessageDTO) throws Exception {
        if (smsMessageUtil.verificationUser(smsMessageDTO)) throw new EntityExistsException();

        String verificationCode = smsMessageUtil.generateVerificationCode();
        String phoneNumber = smsMessageDTO.phoneNumber();

        smsMessageUtil.sendMessage(phoneNumber, verificationCode);
        smsMessageUtil.saveDataForCheckUser(phoneNumber, verificationCode);
    }

    public void getAuthCodeAfterSignup(SMSMessageDTO smsMessageDTO) {
        if (!smsMessageUtil.verificationUser(smsMessageDTO)) throw new EntityNotFoundException();

        String verificationCode = smsMessageUtil.generateVerificationCode();
        String phoneNumber = smsMessageDTO.phoneNumber();

        smsMessageUtil.sendMessage(phoneNumber, verificationCode);
        smsMessageUtil.saveDataForCheckUser(phoneNumber, verificationCode);
    }


    public void checkUsersUsingVerificationCode(SMSVerificationDTO smsVerificationDTO) throws BadRequestException {
        String redisValue = redisUtil.getData(smsVerificationDTO.getPhoneNumber());
        if (!redisValue.equals(smsVerificationDTO.getVerificationCode())) {
            throw new BadRequestException();
        }
    }

    public SMSVerificationResultResponse checkUsersUsingVerificationCodeAfterSignup(SMSVerificationDTO smsVerificationDTO) throws BadRequestException {
        String redisValue = redisUtil.getData(smsVerificationDTO.getPhoneNumber());
        if (!redisValue.equals(smsVerificationDTO.getVerificationCode())) {
            throw new BadRequestException();
        }

        User user = userRepository.findUserByPhoneNumberAndName(smsVerificationDTO.getPhoneNumber(), smsVerificationDTO.getName())
                .orElseThrow(EntityNotFoundException::new);

        SMSVerificationResultResponse response = new SMSVerificationResultResponse(user.getId());
        return response;
    }
}
