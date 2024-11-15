package com.example.jejugudgo.domain.review.service;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseDocumentRepository;
import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseDocumentRepository;
import com.example.jejugudgo.domain.review.docs.ReviewDocument;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import com.example.jejugudgo.domain.trail.repository.TrailDocumentRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewDocumentService {
    private final JejuGudgoCourseDocumentRepository jejuGudgoCourseDocumentRepository;
    private final JejuOlleCourseDocumentRepository jejuOlleCourseDocumentRepository;
    private final TrailDocumentRepository trailDocumentRepository;

    private final ReviewCategoryDocumentService reviewCategoryDocumentService;


    /* review create */
    public ReviewDocument documentsReview(Review review, List<ReviewCategory> categories) {
        JejuGudgoCourseDocument jejuGudgoCourseDocument = null;
        JejuOlleCourseDocument jejuOlleCourseDocument = null;
        TrailDocument trailDocument = null;

        if (review.getJejuGudgoCourse() != null) {
            jejuGudgoCourseDocument = jejuGudgoCourseDocumentRepository.findById(review.getJejuOlleCourse().getId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        }

        if (review.getJejuOlleCourse() != null) {
            jejuOlleCourseDocument = jejuOlleCourseDocumentRepository.findById(review.getJejuOlleCourse().getId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        }

        if (review.getTrail() != null) {
            trailDocument = trailDocumentRepository.findById(review.getTrail().getId())
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        }

        ReviewDocument reviewDocument = ReviewDocument.of(
                review,
                reviewCategoryDocumentService.documentsCategories(categories),
                jejuGudgoCourseDocument,
                jejuOlleCourseDocument,
                trailDocument
        );

        return reviewDocument;
    }
}
