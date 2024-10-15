package com.example.jejugudgo.domain.auth.mail.dto;

public record MailAuthenticationRequest(
        String to,
        String subject
) {
}
