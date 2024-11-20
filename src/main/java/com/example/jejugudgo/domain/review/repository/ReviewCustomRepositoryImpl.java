package com.example.jejugudgo.domain.review.repository;

import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;
import com.example.jejugudgo.domain.review.entity.QReview;
import com.example.jejugudgo.domain.review.entity.QReviewCategory;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<TopFiveRankedKeywordResponse> getTopCategoriesForCourse(ReviewType type, Long courseId) {
        QReview qReview = QReview.review;
        QReviewCategory qReviewCategory = QReviewCategory.reviewCategory;

        Review review = queryFactory
                .selectFrom(qReview)
                .where(getConditionByType(qReview, type, courseId))
                .fetchOne();

        if(review == null) return new ArrayList<>();

        List<TopFiveRankedKeywordResponse> responses = queryFactory
                .select(Projections.constructor(
                        TopFiveRankedKeywordResponse.class,
                        qReviewCategory.category3,
                        qReviewCategory.count().longValue()
                ))
                .from(qReview)
                .join(qReviewCategory).on(qReview.id.eq(qReviewCategory.review.id))
                .where(getConditionByType(qReview, type, courseId))
                .groupBy(qReviewCategory.category3)
                .orderBy(qReviewCategory.count().desc())
                .limit(5)
                .fetch();

        return responses != null ? responses : new ArrayList<>();
    }

    private com.querydsl.core.types.dsl.BooleanExpression getConditionByType(QReview qReview, ReviewType type, Long courseId) {
        if (type == ReviewType.JEJU_GUDGO)
            return qReview.jejuGudgoCourse.id.eq(courseId);

        else if (type == ReviewType.OLLE)
            return qReview.jejuOlleCourse.id.eq(courseId);

        else if (type == ReviewType.TRAIL)
            return qReview.trail.id.eq(courseId);

        return null;
    }
}