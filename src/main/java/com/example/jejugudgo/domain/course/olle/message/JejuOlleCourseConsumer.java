package com.example.jejugudgo.domain.course.olle.message;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseDocumentRepository;
import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.UpdateBookmarkElasticDataRequest;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JejuOlleCourseConsumer {
    private final ObjectMapper objectMapper;
    private final JejuOlleCourseDocumentRepository jejuOlleCourseDocumentRepository;

    @KafkaListener(topics = "olle_topic", groupId = "olle_group")
    public void consumeJejuOlleMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String actionType = jsonNode.get("actionType").asText();
            switch (actionType) {
                case "CREATE":
                    handleCreate(jsonNode.get("data"));
                    break;
                case "UPDATE_STAR_AVG":
                    handleUpdateStarAvg(jsonNode.get("data"));
                    break;
                case "UPDATE_BOOKMARK_USERS":
                    handleUpdateBookmarkUsers(jsonNode.get("data"));
                    break;
                default:
                    throw new CustomException(RetCode.RET_CODE93);
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleCreate(JsonNode course) {
        try {
            JejuOlleCourseDocument jejuOlleCourseDocument= objectMapper.treeToValue(course, JejuOlleCourseDocument.class);

            if (jejuOlleCourseDocument.getTags() == null) {
                jejuOlleCourseDocument.setTags(Collections.emptyList());
            }

            jejuOlleCourseDocumentRepository.save(jejuOlleCourseDocument);

            System.out.println("===============================================================================");
            System.out.println("Olle course saved to Elasticsearch: " + jejuOlleCourseDocument.getId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    public void handleUpdateStarAvg(JsonNode updateRequest) {
        try {
            StarAvgUpdateRequest request = objectMapper.treeToValue(updateRequest, StarAvgUpdateRequest.class);
            JejuOlleCourseDocument courseDocument = jejuOlleCourseDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            courseDocument = courseDocument.updateStarAvg(request.newStarAvg());
            jejuOlleCourseDocumentRepository.save(courseDocument);

            System.out.println("===============================================================================");
            System.out.println("Olle course tar average changed to Elasticsearch: " + courseDocument.getId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleUpdateBookmarkUsers(JsonNode updateRequest) {
        try {
            UpdateBookmarkElasticDataRequest request = objectMapper.treeToValue(updateRequest, UpdateBookmarkElasticDataRequest.class);
            JejuOlleCourseDocument jejuOlleDocument = jejuOlleCourseDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            jejuOlleDocument = jejuOlleDocument.updateBookmarkUsers(request.bookmarkUsers());
            jejuOlleCourseDocumentRepository.save(jejuOlleDocument);

            System.out.println("===============================================================================");
            System.out.println("Jeju olle bookmark users changed to Elasticsearch: " + jejuOlleDocument.getId());
            System.out.println("===============================================================================");

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }
}
