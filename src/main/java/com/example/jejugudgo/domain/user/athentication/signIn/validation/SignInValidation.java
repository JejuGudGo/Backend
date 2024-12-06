package com.example.jejugudgo.domain.user.athentication.signIn.validation;

import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.CurrentSignInStatus;
import com.example.jejugudgo.domain.user.athentication.signIn.enums.TTL;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SignInValidation {
    private final RedisUtil redisUtil;

    private final String PENDING = "PENDING";
    private final String ACTIVE = "ACTIVE";

    public void validateSignInStatus(User user) {
        Long userId = user.getId();
        String status = redisUtil.getData(userId + "_status");

        if (status != null && status.equals("5")) {
            redisUtil.setDataWithExpire(userId + "_status", PENDING, Duration.ofMinutes(1));
            returnSuspendException(PENDING, userId);
        }

        if (status != null && status.equals(PENDING))
            returnSuspendException(PENDING, userId);

        setUserStatus(userId);
    }

    private void setUserStatus(Long userId) {
        String count = redisUtil.getData(userId + "_status");

        if (count == null) count = "1";
        else count = String.valueOf(Integer.parseInt(count) + 1);
        redisUtil.setData(userId + "_status", count);

        returnSuspendException(ACTIVE, userId);
    }

    private void returnSuspendException(String status, Long userId) {
        String exeptionMessge = "";

        if (status.equals(PENDING)) {
            CurrentSignInStatus currentSignInStatus = redisUtil.getKeyTTL(userId + "_status");
            TTL type = currentSignInStatus.type();
            String remainingTime = currentSignInStatus.remainingTime();

            if (type == TTL.SECOND)
                exeptionMessge = RetCode.RET_CODE11.getMessage() + remainingTime + "초 후에 다시 시도하세요.";
            else if (type == TTL.MINUTE)
                exeptionMessge = RetCode.RET_CODE11.getMessage() + remainingTime + "분 후에 다시 시도하세요.";

            throw new CustomException(RetCode.RET_CODE11, exeptionMessge);

        } else if (status.equals(ACTIVE)) {
            String count = redisUtil.getData(userId + "_status");
            exeptionMessge = RetCode.RET_CODE10.getMessage() + "(" + count + "/5)";

            throw new CustomException(RetCode.RET_CODE10, exeptionMessge);
        }
    }
}
