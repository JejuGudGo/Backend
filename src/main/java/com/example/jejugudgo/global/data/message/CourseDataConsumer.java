package com.example.jejugudgo.global.data.message;

import com.example.jejugudgo.global.data.course.OlleCourseElasticDataService;
import com.example.jejugudgo.global.data.course.TrailElasticDataService;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CourseDataConsumer {
    private final ObjectMapper objectMapper;
    private final OlleCourseElasticDataService olleCourseElasticDataService;
    private final TrailElasticDataService trailElasticService;
    
    @KafkaListener(topics = "olle-course", groupId = "olle")
    public void consumeOlleMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String actionType = jsonNode.get("actionType").asText();
            switch (actionType) {
                case "CREATE":
                    olleCourseElasticDataService.createOlleCourseToElastic();
                    break;
                default:
                    log.info("===============================================================================");
                    log.info("Current actionType: {}", actionType);
                    log.info("===============================================================================");
                    throw new CustomException(RetCode.RET_CODE99);
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE96);
        }
    }

    @KafkaListener(topics = "trail-course", groupId = "trail")
    public void consumeTrailMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String actionType = jsonNode.get("actionType").asText();
            switch (actionType) {
                case "CREATE":
                    trailElasticService.createTrailToElastic();
                    break;
                default:
                    log.info("===============================================================================");
                    log.info("Current actionType: {}", actionType);
                    log.info("===============================================================================");
                    throw new CustomException(RetCode.RET_CODE99);
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE96);
        }
    }
}
