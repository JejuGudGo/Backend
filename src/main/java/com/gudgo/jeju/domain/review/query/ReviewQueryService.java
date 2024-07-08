package com.gudgo.jeju.domain.review.query;

import com.gudgo.jeju.domain.review.dto.response.*;
import com.gudgo.jeju.domain.review.entity.*;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<ReviewResponseDto> getReviews(Long plannerId, Pageable pageable) {
        QPlannerReview qPlannerReview = QPlannerReview.plannerReview;

        List<PlannerReview> reviews = queryFactory
                .selectFrom(qPlannerReview)
                .where(qPlannerReview.planner.id.eq(plannerId)
                        .and(qPlannerReview.isDeleted.isFalse()))
                .fetch();

        List<ReviewResponseDto> reviewResponses = reviews.stream()
                .map(this::mapToReviewResponseDto)
                .collect(Collectors.toList());

        return PaginationUtil.listToPage(reviewResponses, pageable);
    }

    public ReviewResponseDto getReview(Long reviewId) {
        QPlannerReview qPlannerReview = QPlannerReview.plannerReview;

        PlannerReview review = queryFactory
                .selectFrom(qPlannerReview)
                .where(qPlannerReview.id.eq(reviewId))
                .fetchOne();

        return mapToReviewResponseDto(review);
    }


    public PlannerReviewCountResponseDto getReviewCount(Long plannerId) {
        QPlannerReview qPlannerReview = QPlannerReview.plannerReview;

        Long count = queryFactory
                .select(qPlannerReview.countDistinct())
                .from(qPlannerReview)
                .where(qPlannerReview.planner.id.eq(plannerId)
                        .and(qPlannerReview.isDeleted.isFalse()))
                .fetchOne();

        return new PlannerReviewCountResponseDto(
                plannerId,
                count
        );
    }


    public Page<ReviewResponseDto> getUserReviews(Long userId, Pageable pageable) {
        QPlannerReview qPlannerReview = QPlannerReview.plannerReview;

        List<PlannerReview> reviews = queryFactory
                .selectFrom(qPlannerReview)
                .where(qPlannerReview.user.id.eq(userId)
                        .and(qPlannerReview.isDeleted).isFalse())
                .fetch();
        List<ReviewResponseDto> reviewResponses = reviews.stream()
                .map(this::mapToReviewResponseDto)
                .collect(Collectors.toList());
        return PaginationUtil.listToPage(reviewResponses,pageable);
    }


    private ReviewResponseDto mapToReviewResponseDto(PlannerReview review) {
        List<ReviewImageResponseDto> reviewImageResponses = fetchReviewImages(review.getId()).stream()
                .map(image -> new ReviewImageResponseDto(
                        image.getId(),
                        image.getPlannerReview().getId(),
                        image.getImageUrl()
                ))
                .collect(Collectors.toList());

        List<ReviewCategoryResponseDto> reviewCategoryResponses = fetchReviewCategories(review.getId()).stream()
                .map(category -> new ReviewCategoryResponseDto(
                        category.getId(),
                        category.getPlannerReview().getId(),
                        category.getCode(),
//                        category.getTitle(),
                        fetchReviewTags(category.getId()).stream()
                                .map(tag -> new ReviewTagResponseDto(
                                        tag.getId(),
                                        tag.getPlannerReviewCategory().getId(),
                                        tag.getCode()
//                                        tag.getTitle()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new ReviewResponseDto(
                review.getId(),
                review.getPlanner().getId(),
                review.getUser().getId(),
                review.getContent(),
                review.getCreatedAt(),
                reviewImageResponses,
                reviewCategoryResponses
        );
    }

    private List<PlannerReviewImage> fetchReviewImages(Long reviewId) {
        QPlannerReviewImage qPlannerReviewImage = QPlannerReviewImage.plannerReviewImage;
        return queryFactory
                .selectFrom(qPlannerReviewImage)
                .where(qPlannerReviewImage.plannerReview.id.eq(reviewId)
                        .and(qPlannerReviewImage.isDeleted.isFalse()))
                .fetch();
    }

    private List<PlannerReviewCategory> fetchReviewCategories(Long reviewId) {
        QPlannerReviewCategory qPlannerReviewCategory = QPlannerReviewCategory.plannerReviewCategory;
        return queryFactory
                .selectFrom(qPlannerReviewCategory)
                .where(qPlannerReviewCategory.plannerReview.id.eq(reviewId))
                .fetch();
    }

    private List<PlannerReviewTag> fetchReviewTags(Long categoryId) {
        QPlannerReviewTag qPlannerReviewTag = QPlannerReviewTag.plannerReviewTag;
        return queryFactory
                .selectFrom(qPlannerReviewTag)
                .where(qPlannerReviewTag.plannerReviewCategory.id.eq(categoryId)
                        .and(qPlannerReviewTag.isDeleted.isFalse()))
                .fetch();
    }
}
