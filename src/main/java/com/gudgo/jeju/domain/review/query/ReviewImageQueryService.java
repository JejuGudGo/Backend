package com.gudgo.jeju.domain.review.query;

import com.gudgo.jeju.domain.review.dto.response.ReviewImageResponseDto;
import com.gudgo.jeju.global.data.review.entity.QReview;
import com.gudgo.jeju.global.data.review.entity.QReviewImage;
import com.gudgo.jeju.domain.review.entity.Review;
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

public class ReviewImageQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewImageQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<ReviewImageResponseDto> getImages(Long plannerId, Pageable pageable) {
        QReview qReview = QReview.review;
        QReviewImage qReviewImage = QReviewImage.reviewImage;

        // 전체 리뷰 가져오기
        List<Review> reviews = queryFactory
                .selectFrom(qReview)
                .where(qReview.planner.id.eq(plannerId)
                        .and(qReview.isDeleted.isFalse()))
                .fetch();

        List<ReviewImageResponseDto> imageResponses = reviews.stream()
                .flatMap(review -> queryFactory
                        .selectFrom(qReviewImage)
                        .where(qReviewImage.review.id.eq(review.getId())
                                .and(qReviewImage.isDeleted.isFalse()))
                        .fetch()
                        .stream().map(image -> new ReviewImageResponseDto(
                                image.getId(),
                                image.getReview().getId(),
                                image.getImageUrl()
                        ))
                ).collect(Collectors.toList());

        return PaginationUtil.listToPage(imageResponses, pageable);
    }
}
