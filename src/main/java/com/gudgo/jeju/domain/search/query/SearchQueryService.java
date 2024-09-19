package com.gudgo.jeju.domain.search.query;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlannerTag;
import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.domain.review.entity.QReviewTag;
import com.gudgo.jeju.domain.review.entity.ReviewFilterTag;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.search.dto.response.SearchListResponse;
import com.gudgo.jeju.domain.trail.entity.*;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchQueryService {
    private static final double EARTH_RADIUS_KM = 6371.0; // 지구 반경 (단위: km)
    private final JPAQueryFactory queryFactory;
    private final ReviewQueryService reviewQueryService;

    @Autowired
    public SearchQueryService(EntityManager entityManager, ReviewQueryService reviewQueryService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.reviewQueryService = reviewQueryService;
    }

    public Page<SearchListResponse> search(String title, String category1, String category2, List<String> category3, Double latitude, Double longitude, Pageable pageable) {
        if (latitude != null && longitude != null) {
            return searchWithCoordinates(title, category1, category2, category3, latitude, longitude, pageable);
        } else {
            return searchWithoutCoordinates(title, category1, category2, category3, pageable);
        }
    }

    private Page<SearchListResponse> searchWithCoordinates(String title, String category1, String category2, List<String> category3, Double latitude, Double longitude, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;
        QTrail qTrail = QTrail.trail;
        QReview qReview = QReview.review;
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        QReviewTag qReviewTag = QReviewTag.reviewTag;
        QTrailTag qTrailTag = QTrailTag.trailTag1;
        QTrailImage qTrailImage = QTrailImage.trailImage;

        List<ReviewFilterTag> reviewFilterTags = category3.stream()
                .map(ReviewFilterTag::valueOf)
                .collect(Collectors.toList());

        if ("OLLE".equalsIgnoreCase(category1)) {
            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag).on(qPlannerTag.planner.eq(qPlanner))
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.olleCourseId.isNotNull())
                            .and(qPlanner.course.originalCreatorId.isNotNull())
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                            .and(withinRadius(qPlanner.course.startLatitude, qPlanner.course.startLongitude, latitude, longitude))
                    )
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToPlannerResponse(planners), pageable);

        } else if ("TRAIL".equalsIgnoreCase(category1)) {
            List<Trail> trails = queryFactory
                    .selectFrom(qTrail)
                    .leftJoin(qTrailTag.trail, qTrail)
                    .leftJoin(qReview.trail, qTrail)
                    .where(qTrail.title.containsIgnoreCase(title)
                            .and(qTrailTag.trailTag.eq(TrailType.valueOf(category2)))
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                            .and(withinRadius(qTrail.latitude, qTrail.longitude, latitude, longitude))
                    )
                    .groupBy(qTrail.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToTrailResponse(trails), pageable);

        } else if ("USER".equalsIgnoreCase(category1)) {
            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag.planner, qPlanner)
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.originalCreatorId.eq(qPlanner.user.id))
                            .and(qPlannerTag.code.eq(PlannerType.valueOf(category2)))
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                            .and(withinRadius(qPlanner.course.startLatitude, qPlanner.course.startLongitude, latitude, longitude))
                    )
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToPlannerResponse(planners), pageable);
        }

        return PaginationUtil.listToPage(List.of(), pageable);
    }

    private Page<SearchListResponse> searchWithoutCoordinates(String title, String category1, String category2, List<String> category3, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;
        QTrail qTrail = QTrail.trail;
        QReview qReview = QReview.review;
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        QReviewTag qReviewTag = QReviewTag.reviewTag;
        QTrailTag qTrailTag = QTrailTag.trailTag1;
        QTrailImage qTrailImage = QTrailImage.trailImage;

        List<ReviewFilterTag> reviewFilterTags = category3.stream()
                .map(ReviewFilterTag::valueOf)
                .collect(Collectors.toList());

        if ("OLLE".equalsIgnoreCase(category1)) {
            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag).on(qPlannerTag.planner.eq(qPlanner))
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.olleCourseId.isNotNull())
                            .and(qPlanner.course.originalCreatorId.isNotNull())
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                    )
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToPlannerResponse(planners), pageable);

        } else if ("TRAIL".equalsIgnoreCase(category1)) {
            List<Trail> trails = queryFactory
                    .selectFrom(qTrail)
                    .leftJoin(qTrailTag.trail, qTrail)
                    .leftJoin(qReview.trail, qTrail)
                    .where(qTrail.title.containsIgnoreCase(title)
                            .and(qTrailTag.trailTag.eq(TrailType.valueOf(category2)))
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                    )
                    .groupBy(qTrail.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToTrailResponse(trails), pageable);

        } else if ("USER".equalsIgnoreCase(category1)) {
            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag.planner, qPlanner)
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.originalCreatorId.eq(qPlanner.user.id))
                            .and(qPlannerTag.code.eq(PlannerType.valueOf(category2)))
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags))
                    )
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            return PaginationUtil.listToPage(convertToPlannerResponse(planners), pageable);
        }

        return PaginationUtil.listToPage(List.of(), pageable);
    }

    private BooleanExpression withinRadius(NumberPath<Double> entityLatitude, NumberPath<Double> entityLongitude, double centerLatitude, double centerLongitude) {
        Expression<Double> distance = Expressions.numberTemplate(Double.class,
                "({0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2}))))",
                EARTH_RADIUS_KM, centerLatitude, entityLatitude, entityLongitude, centerLongitude);

        return Expressions.booleanTemplate("{0} <= 10.0", distance);
    }


    private List<SearchListResponse> convertToPlannerResponse(List<Planner> planners) {
        return planners.stream()
                .map(planner -> {
                    Long reviewCnt = reviewQueryService.getUserCourseReviewCount(planner.getId());
                    List<String> plannerTags = queryFactory
                            .select(Expressions.stringTemplate("str({0})", QPlannerTag.plannerTag.code))
                            .from(QPlannerTag.plannerTag)
                            .where(QPlannerTag.plannerTag.planner.eq(planner))
                            .fetch();

                    return new SearchListResponse(
                            planner.getCourse().getType().toString(),
                            planner.getId(),
                            planner.getCourse().getImageUrl(),
                            planner.getCourse().getTitle(),
                            planner.getCourse().getContent(),
                            planner.getCourse().getTotalDistance(),
                            planner.getTime(),
                            planner.getCourse().getStartLatitude(),
                            planner.getCourse().getStartLongitude(),
                            planner.getCourse().getStarAvg(),
                            reviewCnt,
                            plannerTags
                    );
                }).collect(Collectors.toList());
    }

    private List<SearchListResponse> convertToTrailResponse(List<Trail> trails) {
        return trails.stream()
                .map(trail -> {
                    Long reviewCnt = reviewQueryService.getTrailReviewCount(trail.getId());
                    List<String> trailTags = queryFactory
                            .select(Expressions.stringTemplate("str({0})", QTrailTag.trailTag1.trailTag))
                            .from(QTrailTag.trailTag1)
                            .where(QTrailTag.trailTag1.trail.eq(trail))
                            .fetch();

                    String image = queryFactory
                            .select(QTrailImage.trailImage.imageUrl)
                            .from(QTrailImage.trailImage)
                            .where(QTrailImage.trailImage.trail.eq(trail))
                            .fetchOne();

                    return new SearchListResponse(
                            "TRAIL",
                            trail.getId(),
                            image,
                            trail.getTitle(),
                            trail.getContent(),
                            "None",
                            null,
                            trail.getLatitude(),
                            trail.getLongitude(),
                            trail.getAvgStar(),
                            reviewCnt,
                            trailTags
                    );
                }).collect(Collectors.toList());
    }

    public SearchListResponse getPlanner(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();

        Long reviewCnt = reviewQueryService.getUserCourseReviewCount(plannerId);

        List<String> plannerTags = queryFactory
                .select(Expressions.stringTemplate("str({0})", qPlannerTag.code))
                .from(qPlannerTag)
                .where(qPlannerTag.planner.eq(planner))
                .fetch();

        return new SearchListResponse(
                planner.getCourse().getType().toString(),
                planner.getId(),
                planner.getCourse().getImageUrl(),
                planner.getCourse().getTitle(),
                planner.getCourse().getContent(),
                planner.getCourse().getTotalDistance(),
                planner.getTime(),
                planner.getCourse().getStartLatitude(),
                planner.getCourse().getStartLongitude(),
                planner.getCourse().getStarAvg(),
                reviewCnt,
                plannerTags
        );
    }

    public SearchListResponse getTrail(Long trailId) {
        QTrail qTrail = QTrail.trail;
        QTrailTag qTrailTag = QTrailTag.trailTag1;
        QTrailImage qTrailImage = QTrailImage.trailImage;

        Trail trail = queryFactory
                .selectFrom(qTrail)
                .where(qTrail.id.eq(trailId))
                .fetchOne();

        Long reviewCnt = reviewQueryService.getTrailReviewCount(trailId);

        String image = queryFactory
                .select(qTrailImage.imageUrl)
                .from(qTrailImage)
                .where(qTrailImage.trail.eq(trail))
                .fetchOne();

        List<String> trailTags = queryFactory
                .select(Expressions.stringTemplate("str({0})", qTrailTag.trailTag))
                .from(qTrailTag)
                .where(qTrailTag.trail.eq(trail))
                .fetch();

        return new SearchListResponse(
                "TRAIL",
                trail.getId(),
                image,
                trail.getTitle(),
                trail.getContent(),
                "None",
                null,
                trail.getLatitude(),
                trail.getLongitude(),
                trail.getAvgStar(),
                reviewCnt,
                trailTags
        );
    }
}