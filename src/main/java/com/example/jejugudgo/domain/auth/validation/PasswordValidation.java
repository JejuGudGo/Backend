package com.example.jejugudgo.domain.auth.validation;

import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PasswordValidation {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidation userValidation;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&#]{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);


    public String validatePassword(String password) {
        if (!isValidPassword(password)) {
            throw new CustomException(RetCode.RET_CODE06);  // 비밀번호 형식이 올바르지 않습니다
        }
        return passwordEncoder.encode(password);
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        return pattern.matcher(password).matches();
    }

    public void validatePassword(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            userValidation.setUserStatus(user);
        }
    }
}
