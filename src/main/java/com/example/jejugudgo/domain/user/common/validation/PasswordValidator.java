package com.example.jejugudgo.domain.user.common.validation;


import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PasswordValidator {
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&#]{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public String isPasswordValid(String password) {
        if (!pattern.matcher(password).matches())
            throw new CustomException(RetCode.RET_CODE09);

        return passwordEncoder.encode(password);
    }

    public boolean isPasswordMatch(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}
