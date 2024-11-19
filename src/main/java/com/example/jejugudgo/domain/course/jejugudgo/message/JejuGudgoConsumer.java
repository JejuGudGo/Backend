package com.example.jejugudgo.domain.course.jejugudgo.message;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseDocumentRepository;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.UpdateBookmarkElasticDataRequest;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JejuGudgoConsumer {
    private final ObjectMapper objectMapper;
    private final JejuGudgoCourseDocumentRepository jejuGudgoCourseDocumentRepository;

    @KafkaListener(topics = "jejugudgo_topic", groupId = "jejugudgo_group")
    public void consumeJejuGudgoMessage(String message) {
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
            JejuGudgoCourseDocument courseDocument = objectMapper.treeToValue(course, JejuGudgoCourseDocument.class);
            jejuGudgoCourseDocumentRepository.save(courseDocument);

            System.out.println("===============================================================================");
            System.out.println("Jeju Gudgo course saved to Elasticsearch: " + courseDocument.getId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleUpdateStarAvg(JsonNode updateRequest) {
        try {
            StarAvgUpdateRequest request = objectMapper.treeToValue(updateRequest, StarAvgUpdateRequest.class);
            JejuGudgoCourseDocument courseDocument = jejuGudgoCourseDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            courseDocument = courseDocument.updateStarAvg(request.newStarAvg());
            jejuGudgoCourseDocumentRepository.save(courseDocument);

            System.out.println("===============================================================================");
            System.out.println("Jeju Gudgo star average changed to Elasticsearch: " + courseDocument.getId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleUpdateBookmarkUsers(JsonNode updateRequest) {
        try {
            UpdateBookmarkElasticDataRequest request = objectMapper.treeToValue(updateRequest, UpdateBookmarkElasticDataRequest.class);
            JejuGudgoCourseDocument courseDocument = jejuGudgoCourseDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            courseDocument = courseDocument.updateBookmarkUsers(request.bookmarkUsers());
            jejuGudgoCourseDocumentRepository.save(courseDocument);

            System.out.println("===============================================================================");
            System.out.println("Jeju gudgo course bookmark users changed to Elasticsearch: " + courseDocument.getId());
            System.out.println("===============================================================================");

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    // TODO: elastic 에서 특정 course 수정
    // TODO: elastic 에서 특정 course 삭제
}

