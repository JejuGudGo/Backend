package com.example.jejugudgo.global.message;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@EnableRetry
public class KafkaPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // actionType : CREATE, READ, UPDATE, DELETE

    @Retryable(
            value = { CustomException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void send(String topic, String actionType) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("actionType", actionType);

            setMessage(topic, actionType, message);

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE96);
        }
    }

    @Retryable(
            value = { CustomException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendData(String topic, Object data, String actionType) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("actionType", actionType);
            message.put("data", data);

            setMessage(topic, actionType, message);

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE96);
        }
    }

    private void setMessage(String topic, String actionType, Map<String, Object> message) throws JsonProcessingException {
        String jsonData = objectMapper.writeValueAsString(message);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, jsonData);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                throw new CustomException(RetCode.RET_CODE96);

            } else {
                System.out.println("===============================================================================");
                System.out.println("Message sent successfully: " + topic + " actionType: " + actionType);
                System.out.println("===============================================================================");
            }
        });
    }
}
