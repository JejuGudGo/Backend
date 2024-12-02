package com.example.jejugudgo.domain.auth.validation;

import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserValidation {
    private final RedisUtil redisUtil;

    public void validateUserStatus(User user) {
        String status = redisUtil.getData(user.getId().toString() + "_status");
        String[] TTL = status != null ? redisUtil.getKeyTTL(user.getId().toString() + "_status") : null;

        if (status != null && status.equals("pended"))
            returnSuspendException(status, TTL);
        else
            redisUtil.deleteData(user.getId().toString() + "_password");
    }

    public void setUserStatus(User user) {
        String count = redisUtil.getData(user.getId().toString() + "_password");
        String status = redisUtil.getData(user.getId().toString() + "_status");
        String[] TTL = status != null ? redisUtil.getKeyTTL(user.getId().toString() + "_status") : null;

        returnSuspendException(status, TTL);

        // 1. count 증가
        if (count == null && status == null || count.equals("5"))
            count = "1";
        else
            count = String.valueOf(Integer.parseInt(count) + 1);

        redisUtil.setData(user.getId().toString() + "_password", count);

        // 2. count validation
        if (Integer.parseInt(count) == 5) {
            redisUtil.setDataWithExpire(user.getId().toString() + "_status", "pended", Duration.ofMinutes(1));
            returnSuspendException(status, TTL);
        }

        String message = RetCode.RET_CODE09.getMessage() + "(" + count + "/5)" ;
        throw new CustomException(RetCode.RET_CODE09, message);
    }

    private void returnSuspendException(String status, String[] TTL) {
        if (status != null && status.equals("pended")) {
            String message = "";

            if (TTL[0].equals("sec"))
                message = RetCode.RET_CODE16.getMessage() + TTL[1] + "초 후에 다시 시도하세요.";
            else if (TTL[1].equals("min"))
                message = RetCode.RET_CODE16.getMessage() + TTL[1] + "분 후에 다시 시도하세요.";

            throw new CustomException(RetCode.RET_CODE16, message);
        }
    }
}
