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
    private final RedisUtil redisUtil;

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

    public void validateLoginPassword(String password, User user) {
        String count = redisUtil.getData(user.getId().toString() + "_password");
        String status = redisUtil.getData(user.getId().toString() + "_status");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 1. count 증가
            if (count == null && status == null)
                count = "1";
            else {
                count = String.valueOf(Integer.parseInt(count) + 1);

                // 2. count validation
                if (Integer.parseInt(count) == 5) {
                    redisUtil.setDataWithExpire(user.getId().toString() , "pended", Duration.ofMinutes(10));
                    throw new CustomException(RetCode.RET_CODE16);
                }
            }

            redisUtil.setData(user.getId().toString() + "_password", count);
            String message = RetCode.RET_CODE09.getMessage() + System.lineSeparator()
                    + "현재 비밀번호를 " + count + " 회 잘못 입력하였습니다.";
            System.out.println(message);
            throw new CustomException(RetCode.RET_CODE09, message);
        }
    }
}
