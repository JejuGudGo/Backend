package com.example.jejugudgo.domain.user.athentication.mobile.service;

import com.example.jejugudgo.domain.user.athentication.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.redis.RedisUtil;
import jakarta.annotation.PostConstruct;
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
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class SMSMessageService {
    @Value("${coolsms.api.key}")
    private String apikey;

    @Value("${coolsms.api.secret}")
    private String apiSecretkey;

    @Value("${sendNumber}")
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

    public String generateAuthenticationCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);

        return String.valueOf(randomNumber);
    }

    public void setAuthentcationCode(String phoneNumber, String verificationCode) {
        redisUtil.setDataWithExpire(phoneNumber, verificationCode, Duration.ofMinutes(3));

        log.info("======================================================");
        log.info("CheckRedisKeyValue: {}", redisUtil.getData(phoneNumber));
        log.info("======================================================");
    }
}
