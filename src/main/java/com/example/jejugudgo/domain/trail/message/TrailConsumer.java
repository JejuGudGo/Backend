package com.example.jejugudgo.domain.trail.message;

import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import com.example.jejugudgo.domain.trail.repository.TrailDocumentRepository;
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
public class TrailConsumer {
    private final ObjectMapper objectMapper;
    private final TrailDocumentRepository trailDocumentRepository;

    @KafkaListener(topics = "trail_topic", groupId = "trail_group")
    public void consumeTrailMessage(String message) {
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

    private void handleCreate(JsonNode trail) {
        try {
            TrailDocument trailDocument = objectMapper.treeToValue(trail, TrailDocument.class);

            boolean exists = trailDocumentRepository.existsById(trailDocument.getId());

            if (exists) {
                System.out.println("===============================================================================");
                System.out.println("Trail already exists in Elasticsearch: " + trailDocument.getId());
                System.out.println("===============================================================================");

            } else {
                trailDocumentRepository.save(trailDocument);

                System.out.println("===============================================================================");
                System.out.println("Trail saved to Elasticsearch: " + trailDocument.getId());
                System.out.println("===============================================================================");
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleUpdateStarAvg(JsonNode updateRequest) {
        try {
            StarAvgUpdateRequest request = objectMapper.treeToValue(updateRequest, StarAvgUpdateRequest.class);
            TrailDocument trailDocument = trailDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            trailDocument = trailDocument.updateStarAvg(request.newStarAvg());
            trailDocumentRepository.save(trailDocument);

            System.out.println("===============================================================================");
            System.out.println("Trail star average changed to Elasticsearch: " + trailDocument.getId());
            System.out.println("===============================================================================");

        } catch (JsonProcessingException e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }

    private void handleUpdateBookmarkUsers(JsonNode updateRequest) {
        try {
            UpdateBookmarkElasticDataRequest request = objectMapper.treeToValue(updateRequest, UpdateBookmarkElasticDataRequest.class);
            TrailDocument trailDocument = trailDocumentRepository.findById(request.courseId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            trailDocumentRepository.save(trailDocument);

            System.out.println("===============================================================================");
            System.out.println("Trail bookmark users changed to Elasticsearch: " + trailDocument.getId());
            System.out.println("===============================================================================");

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE95);
        }
    }
}
