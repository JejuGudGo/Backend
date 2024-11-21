package com.example.jejugudgo.domain.review.message;

import com.example.jejugudgo.domain.review.docs.ReviewDocument;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import com.example.jejugudgo.domain.review.service.ReviewDocumentService;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewPublisher {
    private final KafkaPublisher kafkaPublisher;
    private final ReviewDocumentService reviewDocumentService;

    public void createReviewMessagePublish(Review review, List<ReviewCategory> categories) {
        ReviewDocument reviewDocument = reviewDocumentService.documentsReview(review, categories);
        kafkaPublisher.sendData("review_topic", reviewDocument, "CREATE");
    }
}
