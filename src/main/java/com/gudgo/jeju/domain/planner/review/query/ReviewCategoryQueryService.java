package com.gudgo.jeju.domain.planner.review.query;

import com.gudgo.jeju.domain.planner.review.dto.response.ReviewCategoryResponseDto;
import com.gudgo.jeju.domain.planner.review.dto.response.ReviewTagResponseDto;
import com.gudgo.jeju.domain.planner.review.entity.PlannerReviewCategory;
import com.gudgo.jeju.domain.planner.review.entity.PlannerReviewTag;
import com.gudgo.jeju.domain.review.entity.QPlannerReviewCategory;
import com.gudgo.jeju.domain.review.entity.QPlannerReviewTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewCategoryQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewCategoryQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<ReviewCategoryResponseDto> getReviewCategories(Long reviewId) {
        QPlannerReviewCategory qPlannerReviewCategory = QPlannerReviewCategory.plannerReviewCategory;
        QPlannerReviewTag qPlannerReviewTag = QPlannerReviewTag.plannerReviewTag;

        List<PlannerReviewCategory> reviewCategories = queryFactory
                .selectFrom(qPlannerReviewCategory)
                .where(qPlannerReviewCategory.plannerReview.id.eq(reviewId))
                .fetch();

        List<ReviewCategoryResponseDto> reviewCategoryResponses = reviewCategories.stream()
                .map(reviewCategory -> {
                    List<PlannerReviewTag> reviewTags = queryFactory
                            .selectFrom(qPlannerReviewTag)
                            .where(qPlannerReviewTag.plannerReviewCategory.id.eq(reviewCategory.getId())
                                    .and(qPlannerReviewTag.isDeleted.isFalse()))
                            .fetch();

                    List<ReviewTagResponseDto> reviewTagResponses = reviewTags.stream()
                            .map(tag -> new ReviewTagResponseDto(
                                    tag.getId(),
                                    tag.getPlannerReviewCategory().getId(),
                                    tag.getCode()
                            )).collect(Collectors.toList());


                    return new ReviewCategoryResponseDto(
                            reviewCategory.getId(),
                            reviewCategory.getPlannerReview().getId(),
                            reviewCategory.getCode(),
                            reviewTagResponses
                    );
                }).collect(Collectors.toList());

        return reviewCategoryResponses;
    }

}
