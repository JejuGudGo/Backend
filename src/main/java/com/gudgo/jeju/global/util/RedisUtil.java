package com.gudgo.jeju.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;

    public String getData(String key) {
        valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataWithExpire(String key, String value, Duration duration) {
        valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
