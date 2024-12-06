package com.example.jejugudgo.domain.user.athentication.validation;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationValidator {
    private final RedisUtil redisUtil;

    public void isAuthenticationCodeValid(AuthenticationCodeRequest request) {
        String redisValue = redisUtil.getData(request.key());
        String requestValue = request.value();

        if (redisValue == null)
            throw new CustomException(RetCode.RET_CODE03);

        if (!isValidAuthCodeFormat(requestValue)) {
            throw new CustomException(RetCode.RET_CODE01);
        }

        if (!redisValue.equals(requestValue)) {
            throw new CustomException(RetCode.RET_CODE02);
        }
    }

    private boolean isValidAuthCodeFormat(String authCode) {
        return authCode.matches("\\d{6}");
    }
}
