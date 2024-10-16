package com.example.jejugudgo.domain.auth.mail.service;

import com.example.jejugudgo.domain.auth.mail.dto.MailAuthenticationRequest;
import com.example.jejugudgo.global.redis.RedisUtil;
import com.example.jejugudgo.global.util.RandomNumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailSendService {
    private final JavaMailSender javaMailSender;
    private final RandomNumberUtil randomNumberUtil;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;

    public String sendAuthCode(MailAuthenticationRequest request) throws MessagingException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Long authCode = randomNumberUtil.setRandomCode();
        redisUtil.setData(request.to(), String.valueOf(authCode));

        mimeMessageHelper.setTo(request.to());
        mimeMessageHelper.setSubject(request.subject());
        mimeMessageHelper.setText(setContext(String.valueOf(authCode)),true);
        javaMailSender.send(mimeMessage);

        return String.valueOf(authCode);
    }

    private String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("emailAuthentication", context);
    }
}
