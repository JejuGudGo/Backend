package com.gudgo.jeju.global.util.mail.dto.request;

public record MailAuthenticationMessage(
        String to,
        String subject
) {
}
