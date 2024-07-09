package com.gudgo.jeju.domain.review.query;

import com.gudgo.jeju.domain.review.dto.response.ReviewImageResponseDto;
import com.gudgo.jeju.domain.review.entity.PlannerReview;
import com.gudgo.jeju.domain.review.entity.PlannerReviewImage;
import com.gudgo.jeju.domain.review.entity.QPlannerReview;
import com.gudgo.jeju.domain.review.entity.QPlannerReviewImage;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ReviewImageQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewImageQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<ReviewImageResponseDto> getImages(Long plannerId, Pageable pageable) {
        QPlannerReview qPlannerReview = QPlannerReview.plannerReview;
        QPlannerReviewImage qPlannerReviewImage = QPlannerReviewImage.plannerReviewImage;


        // 전체 리뷰 가져오기
        List<PlannerReview> reviews = queryFactory
                .selectFrom(qPlannerReview)
                .where(qPlannerReview.planner.id.eq(plannerId)
                        .and(qPlannerReview.isDeleted.isFalse()))
                .fetch();

        List<ReviewImageResponseDto> imageResponses = reviews.stream()
                .flatMap(review -> queryFactory
                        .selectFrom(qPlannerReviewImage)
                        .where(qPlannerReviewImage.plannerReview.id.eq(review.getId())
                                .and(qPlannerReviewImage.isDeleted.isFalse()))
                        .fetch()
                        .stream().map(image -> new ReviewImageResponseDto(
                                image.getId(),
                                image.getPlannerReview().getId(),
                                image.getImageUrl()
                        ))
                ).collect(Collectors.toList());

        return PaginationUtil.listToPage(imageResponses, pageable);
    }

    public List<ReviewImageResponseDto> getReviewImages(Long plannerReviewId) {
        QPlannerReviewImage qPlannerReviewImage = QPlannerReviewImage.plannerReviewImage;

        List<PlannerReviewImage> reviewImages = queryFactory
                .selectFrom(qPlannerReviewImage)
                .where(qPlannerReviewImage.plannerReview.id.eq(plannerReviewId)
                        .and(qPlannerReviewImage.isDeleted.isFalse()))

                .fetch();

        List<ReviewImageResponseDto> reviewImageResponses = reviewImages.stream()
                .map(reviewImage -> new ReviewImageResponseDto(
                        reviewImage.getId(),
                        reviewImage.getPlannerReview().getId(),
                        reviewImage.getImageUrl()
                )).toList();

        return reviewImageResponses;
    }

    public ReviewImageResponseDto getReviewImage(Long imageId) {
        QPlannerReviewImage qPlannerReviewImage = QPlannerReviewImage.plannerReviewImage;

        PlannerReviewImage reviewImage = queryFactory
                .selectFrom(qPlannerReviewImage)
                .where(qPlannerReviewImage.id.eq(imageId))
                .fetchOne();


        return new ReviewImageResponseDto(
                reviewImage.getId(),
                reviewImage.getPlannerReview().getId(),
                reviewImage.getImageUrl()
        );
    }
}
