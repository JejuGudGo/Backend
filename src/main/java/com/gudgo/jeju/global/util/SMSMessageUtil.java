package com.gudgo.jeju.global.util;

import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class SMSMessageUtil {
    @Value("${cool.sms.key}")
    private String apikey;

    @Value("${cool.sms.secret.key}")
    private String apiSecretkey;

    @Value("${send.number}")
    private String sender;

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp
                .INSTANCE
                .initialize(apikey, apiSecretkey, "https://api.coolsms.co.kr");
    }

    public boolean verificationUser(SMSMessageDTO smsMessageDTO) {
        String phoneNumber = smsMessageDTO.phoneNumber();
        String name = smsMessageDTO.name();

        Optional<User> user = userRepository.findUserByPhoneNumberAndName(phoneNumber, name);

        return user.isPresent();
    }

    public SingleMessageSentResponse sendMessage(String sendTo, String verificationCode) {
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(sendTo);
        message.setText("[제주걷GO] 인증번호는 " + verificationCode + " 입니다. 정확히 입력해주세요.");

        SingleMessageSentResponse response = this.messageService
                .sendOne(new SingleMessageSendingRequest(message));

        log.info("======================================================");
        log.info("SMSMessage sent to: " + sendTo);
        log.info("======================================================");

        return response;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);

        return String.valueOf(randomNumber);
    }

    public void saveDataForCheckUser(String phoneNumber, String verificationCode) {
        redisUtil.setDataWithExpire(phoneNumber, verificationCode, Duration.ofMinutes(3));

        log.info("======================================================");
        log.info("CheckRedisKeyValue: " + redisUtil.getData(phoneNumber));
        log.info("======================================================");
    }
}
