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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final ReviewQueryService reviewQueryService;

    @Autowired
    public SearchQueryService(EntityManager entityManager, ReviewQueryService reviewQueryService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.reviewQueryService = reviewQueryService;
    }

    public Page<SearchListResponse> search(String title, String category1, String category2, List<String> category3, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;
        QTrail qTrail = QTrail.trail;
        QReview qReview = QReview.review;

        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        QReviewTag qReviewTag = QReviewTag.reviewTag;
        QTrailTag qTrailTag = QTrailTag.trailTag1;
        QTrailImage qTrailImage = QTrailImage.trailImage;

        List<SearchListResponse> responses = List.of();

        if ("OLLE".equalsIgnoreCase(category1)) {
            List<ReviewFilterTag> reviewFilterTags = category3.stream()
                    .map(tag -> ReviewFilterTag.valueOf(tag))
                    .collect(Collectors.toList());

            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag).on(qPlannerTag.planner.eq(qPlanner))
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.olleCourseId.isNotNull())
                            .and(qPlanner.course.originalCreatorId.isNotNull()) // 원작자에만 creatorId 를 넣을 생각입니다.
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags)))
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            List<SearchListResponse> plannerResopnses = planners.stream()
                    .map(planner -> {
                        Long reviewCnt = reviewQueryService.getUserCourseReviewCount(planner.getId());
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
                                planner.getCourse().getStarAvg(),
                                reviewCnt,
                                plannerTags
                        );
                    }).toList();

            return PaginationUtil.listToPage(plannerResopnses, pageable);

        } else if ("TRAIL".equalsIgnoreCase(category1)) {
            List<ReviewFilterTag> reviewFilterTags = category3.stream()
                    .map(tag -> ReviewFilterTag.valueOf(tag))
                    .collect(Collectors.toList());

            List<Trail> trails = queryFactory
                    .selectFrom(qTrail)
                    .leftJoin(qTrailTag.trail, qTrail)
                    .leftJoin(qReview.trail, qTrail)
                    .where(qTrail.title.containsIgnoreCase(title)
                            .and(qTrailTag.trailTag.eq(TrailType.valueOf(category2)))
                            .and(qReviewTag.filterTag.in(reviewFilterTags)))
                    .groupBy(qTrail.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            List<SearchListResponse> TrailResponses = trails.stream()
                    .map(trail -> {
                        Long reviewCnt = reviewQueryService.getTrailReviewCount(trail.getId());
                        List<String> trailTags = queryFactory
                                .select(Expressions.stringTemplate("str({0})", qPlannerTag.code))
                                .from(qTrailTag)
                                .where(qTrailTag.trail.eq(trail))
                                .fetch();

                        String image = queryFactory
                                .select(qTrailImage.imageUrl)
                                .from(qTrailImage)
                                .fetchOne();

                        return new SearchListResponse(
                                "TRAIL",
                                trail.getId(),
                                image,
                                trail.getTitle(),
                                trail.getContent(),
                                "None",
                                null,
                                trail.getAvgStar(),
                                reviewCnt,
                                trailTags
                        );
                    }).toList();

            return PaginationUtil.listToPage(TrailResponses, pageable);

        } else if ("USER".equalsIgnoreCase(category1)) {
            List<ReviewFilterTag> reviewFilterTags = category3.stream()
                    .map(tag -> ReviewFilterTag.valueOf(tag))
                    .collect(Collectors.toList());

            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .leftJoin(qReviewTag.review, qReview)
                    .leftJoin(qPlannerTag.planner, qPlanner)
                    .where(qPlanner.course.title.containsIgnoreCase(title)
                            .and(qPlanner.course.originalCreatorId.eq(qPlanner.user.id))
                            .and(qPlannerTag.code.eq(PlannerType.valueOf(category2)))
                            .and(qPlanner.isDeleted.isFalse())
                            .and(qReviewTag.filterTag.in(reviewFilterTags)))
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            List<SearchListResponse> plannerResponses = planners.stream()
                    .map(planner -> {
                        Long reviewCnt = reviewQueryService.getUserCourseReviewCount(planner.getId());
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
                                "None",
                                planner.getTime(),
                                planner.getCourse().getStarAvg(),
                                reviewCnt,
                                plannerTags
                        );
                    }).toList();

            return PaginationUtil.listToPage(plannerResponses, pageable);

        } else if ("ALL".equalsIgnoreCase(category1)) {
            List<Planner> planners = queryFactory
                    .selectFrom(qPlanner)
                    .leftJoin(qReview.planner, qPlanner)
                    .where(qPlanner.course.originalCreatorId.eq(qPlanner.user.id)
                            .or(qPlanner.course.olleCourseId.isNotNull()
                                    .and(qPlanner.course.originalCreatorId.isNotNull())
                                    .and(qPlanner.isDeleted.isFalse())))
                    .groupBy(qPlanner.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            List<Trail> trails = queryFactory
                    .selectFrom(qTrail)
                    .leftJoin(qTrailTag.trail, qTrail)
                    .leftJoin(qReview.trail, qTrail)
                    .groupBy(qTrail.id)
                    .orderBy(qReview.count().desc())
                    .fetch();

            List<SearchListResponse> plannerResponses = planners.stream()
                    .map(planner -> {
                        Long reviewCnt = reviewQueryService.getUserCourseReviewCount(planner.getId());
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
                                planner.getCourse().getStarAvg(),
                                reviewCnt,
                                plannerTags
                        );
                    }).toList();

            List<SearchListResponse> trailResponses = trails.stream()
                    .map(trail -> {
                        Long reviewCnt = reviewQueryService.getTrailReviewCount(trail.getId());
                        List<String> trailTags = queryFactory
                                .select(Expressions.stringTemplate("str({0})", qTrailTag.trailTag))
                                .from(qTrailTag)
                                .where(qTrailTag.trail.eq(trail))
                                .fetch();

                        String image = queryFactory
                                .select(qTrailImage.imageUrl)
                                .from(qTrailImage)
                                .where(qTrailImage.trail.eq(trail))
                                .fetchOne();

                        return new SearchListResponse(
                                "TRAIL",
                                trail.getId(),
                                image,
                                trail.getTitle(),
                                trail.getContent(),
                                "None",
                                null,
                                trail.getAvgStar(),
                                reviewCnt,
                                trailTags
                        );
                    }).toList();

            responses = Stream.concat(plannerResponses.stream(), trailResponses.stream())
                    .sorted(Comparator.comparingLong(SearchListResponse::reviewCount).reversed())
                    .collect(Collectors.toList());
        }

        return PaginationUtil.listToPage(responses, pageable);
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
                trail.getAvgStar(),
                reviewCnt,
                trailTags
        );
    }
}
