package com.gudgo.jeju.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();


    public String getData(String key) {
        valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // Redis에서 값을 가져와서 객체로 변환
    public <T> T getObjectData(String contentId, Class<T> clazz) throws JsonProcessingException {
        String value = getData(contentId);

        if (value != null) {
            return objectMapper.readValue(value, clazz);
        }

        return null;
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
