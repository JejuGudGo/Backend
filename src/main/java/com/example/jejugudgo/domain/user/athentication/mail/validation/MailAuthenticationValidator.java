package com.example.jejugudgo.domain.user.athentication.mail.validation;

import com.example.jejugudgo.domain.user.athentication.mail.dto.EmailAuthenticationRequest;
import com.example.jejugudgo.domain.user.athentication.validation.AuthenticationCodeRequest;
import com.example.jejugudgo.domain.user.athentication.validation.AuthenticationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailAuthenticationValidator {
    private final AuthenticationValidator authenticationValidator;

    public void validateAuthenticationCode(EmailAuthenticationRequest request) {
        String key = request.email();
        String value = request.authCode();

        AuthenticationCodeRequest authenticationCodeRequest = new AuthenticationCodeRequest(key, value);
        authenticationValidator.isAuthenticationCodeValid(authenticationCodeRequest);
    }
}
