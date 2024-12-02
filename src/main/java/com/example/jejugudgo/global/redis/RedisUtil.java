package com.example.jejugudgo.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;

    public String getData(String key) {
        valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public String[] getKeyTTL(String key) {
        Long minutes = stringRedisTemplate.getExpire(key, TimeUnit.MINUTES);
        if (minutes < 1) {
            return new String[] {"sec", stringRedisTemplate.getExpire(key, TimeUnit.SECONDS).toString()};
        } else {
            return  new String[] {"min", minutes.toString()};
        }
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
