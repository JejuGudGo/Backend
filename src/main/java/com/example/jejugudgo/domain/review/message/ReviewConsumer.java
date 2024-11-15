package com.example.jejugudgo.domain.review.message;

import com.example.jejugudgo.domain.review.docs.ReviewDocument;
import com.example.jejugudgo.domain.review.repository.ReviewDocumentRepository;
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
public class ReviewConsumer {
    private final ObjectMapper objectMapper;
    private final ReviewDocumentRepository reviewDocumentRepository;

    @KafkaListener(topics = "review_topic", groupId = "review_group")
    public void consumeReviewMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String actionType = jsonNode.get("actionType").asText();

            switch (actionType) {
                case "CREATE":
                    handleCreate(jsonNode.get("data"));
                    break;
                case "UPDATE":
                    handleUpdate(jsonNode.get("data"));
                    break;
                case "DELETE":
                    handleDelete(jsonNode.get("reviewId").asLong());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action type: " + actionType);
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleCreate(JsonNode review) {
        try {
            ReviewDocument reviewDocument = objectMapper.treeToValue(review, ReviewDocument.class);
            reviewDocumentRepository.save(reviewDocument);

            System.out.println("===============================================================================");
            System.out.println("Review saved to Elasticsearch: " + reviewDocument.getReviewId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    // TODO: elastic 에서 특정 review 수정
    private void handleUpdate(JsonNode review) {

    }

    // TODO: elastic 에서 특정 review 삭제
    private void handleDelete(Long reviewId) {

    }
}
