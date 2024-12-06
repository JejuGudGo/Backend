package com.example.jejugudgo.domain.user.common.validation;

import com.example.jejugudgo.domain.user.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationManager {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final PhoneNumValidator phoneNumValidator;

    public void validateEmailDuplication(String email) {
        emailValidator.isEmailDuplicated(email);
    }

    public String validatePasswordPattern(String password) {
        return passwordValidator.isPasswordValid(password);
    }

    public boolean validateSignInPassword(User user, String password) {
        return passwordValidator.isPasswordMatch(user, password);
    }

    public void validatePhoneNumPattern(String phoneNum) {
        phoneNumValidator.isValidPhoneNum(phoneNum);
    }
}
