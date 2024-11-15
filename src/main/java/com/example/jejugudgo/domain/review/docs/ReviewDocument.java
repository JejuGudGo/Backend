package com.example.jejugudgo.domain.review.docs;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "review")
@Data
public class ReviewDocument {
    @Id
    private Long reviewId;

    private String content;

    private LocalDate createdAt;

    private LocalDate finishedAt;

    private Long stars;

    private JejuGudgoCourseDocument jejuGudgoCourseDocument;

    private JejuOlleCourseDocument jejuOlleCourseDocument;

    private TrailDocument trailDocument;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<ReviewCategoryDocument> categories;


    public static ReviewDocument of(
            Review review,
            List<ReviewCategoryDocument> categories,
            JejuGudgoCourseDocument jejuGudgoCourseDocument,
            JejuOlleCourseDocument jejuOlleCourseDocument,
            TrailDocument trailDocument
    ) {
        ReviewDocument reviewDocument = new ReviewDocument();
        reviewDocument.setReviewId(review.getId());
        reviewDocument.setContent(review.getContent());
        reviewDocument.setCreatedAt(review.getCreatedAt());
        reviewDocument.setFinishedAt(review.getFinishedAt());
        reviewDocument.setStars(review.getStars());
        reviewDocument.setJejuGudgoCourseDocument(jejuGudgoCourseDocument);
        reviewDocument.setJejuOlleCourseDocument(jejuOlleCourseDocument);
        reviewDocument.setTrailDocument(trailDocument);
        reviewDocument.setCategories(categories);

        return reviewDocument;
    }
}
