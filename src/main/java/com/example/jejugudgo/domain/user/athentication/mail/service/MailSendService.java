package com.example.jejugudgo.domain.user.athentication.mail.service;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.redis.RedisUtil;
import com.example.jejugudgo.global.util.random.RandomNumberUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MailSendService {
    private final JavaMailSender javaMailSender;
    private final RandomNumberUtil randomNumberUtil;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;

    private final String MESSAGE_HEADER = "[제주걷GO] ";
    private final String MESSAGE_BODY = " 이(가) 인증 코드 입니다.";

    public void sendAuthenticationCode(String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Long authCode = randomNumberUtil.setRandomCode();
            redisUtil.setDataWithExpire(email, String.valueOf(authCode), Duration.ofMinutes(3));

            String message = MESSAGE_HEADER + authCode + MESSAGE_BODY;
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(message);
            mimeMessageHelper.setText(setContext(String.valueOf(authCode)), true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    private String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("emailAuthentication", context);
    }
}
