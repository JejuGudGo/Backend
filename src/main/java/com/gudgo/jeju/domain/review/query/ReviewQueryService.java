package com.gudgo.jeju.domain.review.query;

import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;
import com.gudgo.jeju.domain.review.dto.response.TopRatingTagResponseDto;
import com.gudgo.jeju.domain.review.entity.*;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReviewQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReviewQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 산책로, 유저 코스 리뷰 전부 포함
    public Page<ReviewResponse> getUserReviews(Long userId, Pageable pageable) {
        QReview qReview = QReview.review;

        List<Review> reviews = queryFactory
                .selectFrom(qReview)
                .where(qReview.user.id.eq(userId)
                        .and(qReview.isDeleted.isFalse()))
                .fetch();

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> {

                    String userNickname = validateNickname(review);
                    String title = validateTitle(review);
                    String userProfileImgUrl = "";

                    if (userNickname.equals("제주걷고")) userProfileImgUrl = "None";
                    else userProfileImgUrl = review.getUser().getProfile().getProfileImageUrl();

                    QReviewTag qReviewTag = QReviewTag.reviewTag;
                    List<ReviewFilterTag> tags = queryFactory
                            .select(qReviewTag.filterTag)
                            .from(qReviewTag)
                            .where(qReviewTag.review.eq(review)
                                    .and(qReviewTag.review.isDeleted.isFalse()))
                            .fetch();

                    QReviewImage qReviewImage = QReviewImage.reviewImage;
                    List<String> images = queryFactory
                            .select(qReviewImage.imageUrl)
                            .from(qReviewImage)
                            .where(qReviewImage.review.eq(review)
                                    .and(qReviewImage.review.isDeleted.isFalse()))
                            .fetch();

                    return new ReviewResponse(
                            review.getId(),
                            title,
                            userNickname,
                            review.getCreatedAt(),
                            userProfileImgUrl,
                            review.getStars(),
                            tags,
                            review.getContent(),
                            images
                    );
                }).toList();

        return PaginationUtil.listToPage(reviewResponses, pageable);
    }

    private String validateNickname(Review review) {
        Long orgUserId = review.getPlanner().getCourse().getOriginalCreatorId();

        if (orgUserId != null) {
            QUser qUser = QUser.user;

            User orgUser = queryFactory
                    .selectFrom(qUser)
                    .where(qUser.id.eq(orgUserId))
                    .fetchOne();

            return orgUser.getNickname();
        }

        return "제주걷고";
    }

    private String validateTitle(Review review) {
        if (review.getPlanner() != null) return review.getPlanner().getCourse().getTitle();
        else return review.getTrail().getTitle();
    }

    public Long getTrailReviewCount(Long trailId) {
        QReview qReview = QReview.review;

        Long count = queryFactory
                .select(qReview.countDistinct())
                .from(qReview)
                .where(qReview.trail.id.eq(trailId)
                        .and(qReview.isDeleted.isFalse()))
                .fetchOne();

        return count;
    }

    public Long getUserCourseReviewCount(Long plannerId) {
        QReview qReview = QReview.review;

        Long count = queryFactory
                .select(qReview.countDistinct())
                .from(qReview)
                .where(qReview.planner.id.eq(plannerId)
                        .and(qReview.isDeleted.isFalse()))
                .fetchOne();

        return count;
    }

    public List<TopRatingTagResponseDto> findTrailTop5ReviewTags(Long trailId) {
        QReviewTag reviewTag = QReviewTag.reviewTag;

        return queryFactory
                .select(Projections.constructor(TopRatingTagResponseDto.class,
                        reviewTag.filterTag,
                        reviewTag.count()))
                .from(reviewTag)
                .join(reviewTag.review, QReview.review)
                .where(reviewTag.review.trail.id.eq(trailId)
                        .and(reviewTag.isDeleted.eq(false)))
                .groupBy(reviewTag.filterTag)
                .orderBy(reviewTag.count().desc())
                .limit(5)
                .fetch();
    }

    public List<TopRatingTagResponseDto> findUserPlannerTop5ReviewTags(Long plannerId) {
        QReviewTag reviewTag = QReviewTag.reviewTag;

        return queryFactory
                .select(Projections.constructor(TopRatingTagResponseDto.class,
                        reviewTag.filterTag,
                        reviewTag.count()))
                .from(reviewTag)
                .join(reviewTag.review, QReview.review)
                .where(reviewTag.review.planner.id.eq(plannerId)
                        .and(reviewTag.isDeleted.eq(false)))
                .groupBy(reviewTag.filterTag)
                .orderBy(reviewTag.count().desc())
                .limit(5)
                .fetch();
    }

    public List<ReviewResponse> getTrailReview(Long trailId) { // 일단 3 개로 둠
        QReview qReview = QReview.review;

        List<Review> reviews = queryFactory
                .selectFrom(qReview)
                .where(qReview.trail.id.eq(trailId)
                        .and(qReview.isDeleted.isFalse()))
                .orderBy(qReview.createdAt.desc())
                .limit(3)
                .fetch();

        return getReviewResponses(reviews);
    }

    public List<ReviewResponse> getPlannerReview(Long plannerId) { // 일단 10 개로 둠
        QReview qReview = QReview.review;

        List<Review> reviews = queryFactory
                .selectFrom(qReview)
                .where(qReview.planner.id.eq(plannerId)
                        .and(qReview.isDeleted.isFalse()))
                .orderBy(qReview.createdAt.desc())
                .limit(10)
                .fetch();

        return getReviewResponses(reviews);
    }

    @NotNull
    private List<ReviewResponse> getReviewResponses(List<Review> reviews) {
        List<ReviewResponse> responses = reviews.stream()
                .map(review -> {

                    QReviewTag qReviewTag = QReviewTag.reviewTag;
                    List<ReviewFilterTag> tags = queryFactory
                            .select(qReviewTag.filterTag)
                            .from(qReviewTag)
                            .where(qReviewTag.review.eq(review)
                                    .and(qReviewTag.review.isDeleted.isFalse()))
                            .fetch();

                    QReviewImage qReviewImage = QReviewImage.reviewImage;
                    List<String> images = queryFactory
                            .select(qReviewImage.imageUrl)
                            .from(qReviewImage)
                            .where(qReviewImage.review.eq(review)
                                    .and(qReviewImage.review.isDeleted.isFalse()))
                            .fetch();

                    return new ReviewResponse(
                            review.getId(),
                            review.getTrail().getTitle(),
                            review.getUser().getNickname(),
                            review.getCreatedAt(),
                            review.getUser().getProfile().getProfileImageUrl(),
                            review.getStars(),
                            tags,
                            review.getContent(),
                            images
                    );
                }).toList();

        return responses;
    }
}
