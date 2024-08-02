package com.gudgo.jeju.global.util.mail.service;


import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import com.gudgo.jeju.global.util.RedisUtil;
import com.gudgo.jeju.global.util.mail.dto.request.MailAuthenticationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RandomNumberUtil randomNumberUtil;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;

    public String sendMailAuthenticationCode(MailAuthenticationMessage message) throws MessagingException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        Long authCode = randomNumberUtil.setRandomCode();
        redisUtil.setData(message.to(), String.valueOf(authCode));

        mimeMessageHelper.setTo(message.to());
        mimeMessageHelper.setSubject(message.subject());
        mimeMessageHelper.setText(setContext(String.valueOf(authCode)));
        javaMailSender.send(mimeMessage);

        return String.valueOf(authCode);
    }

    private String setContext(String authCode) {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("emailAuthentication", context);
    }
}
