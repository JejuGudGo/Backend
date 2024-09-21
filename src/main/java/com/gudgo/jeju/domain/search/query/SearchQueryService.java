package com.gudgo.jeju.domain.search.query;

import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlannerTag;
import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.domain.review.entity.QReviewTag;
import com.gudgo.jeju.domain.review.entity.ReviewFilterTag;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.search.dto.response.SearchListResponse;
import com.gudgo.jeju.domain.trail.entity.QTrail;
import com.gudgo.jeju.domain.trail.entity.QTrailImage;
import com.gudgo.jeju.domain.trail.entity.QTrailTag;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
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

    public Page<SearchListResponse> search(String title, String category1, List<String> category2, List<String> category3, String latitude, String longitude, Pageable pageable) {
        JPAQuery<Planner> query = queryFactory.selectFrom(QPlanner.planner)
                .leftJoin(QCourse.course).on(QPlanner.planner.course.eq(QCourse.course))
                .where(QPlanner.planner.isDeleted.eq(false));

        if (category1.equals("USER")) {
            query.where(QCourse.course.type.eq(CourseType.USER)
                    .and(QCourse.course.originalCreatorId.eq(QPlanner.planner.user.id)));

        } else if (category1.equals("OLLE")) {
            query.where(QCourse.course.type.eq(CourseType.OLLE)
                    .and(QCourse.course.originalCreatorId.isNotNull())
                    .and(QCourse.course.olleCourseId.isNotNull()));

        } else if (category1.equals("TRAIL")) {
            return PaginationUtil.listToPage(convertToTrailResponse(
                    queryFactory.selectFrom(QTrail.trail).fetch()), pageable);

        } else if (category1.equals("ALL")) {
            List<SearchListResponse> userAndOlleResults = convertToPlannerResponse(
                    queryFactory.selectFrom(QPlanner.planner)
                            .leftJoin(QCourse.course).on(QPlanner.planner.course.eq(QCourse.course))
                            .where(QPlanner.planner.isDeleted.eq(false))
                            .fetch());

            List<SearchListResponse> trailResults = convertToTrailResponse(
                    queryFactory.selectFrom(QTrail.trail)
                            .fetch());

            userAndOlleResults.addAll(trailResults);

            return PaginationUtil.listToPage(userAndOlleResults, pageable);
        }

        if (category2 != null && !category2.isEmpty()) {
            List<PlannerType> category2EnumList = category2.stream()
                    .map(PlannerType::valueOf)
                    .collect(Collectors.toList());

            query.leftJoin(QPlannerTag.plannerTag)
                    .on(QPlanner.planner.eq(QPlannerTag.plannerTag.planner))
                    .where(QPlannerTag.plannerTag.code.in(category2EnumList));
        }

        if (category3 != null && !category3.isEmpty()) {
            List<ReviewFilterTag> category3EnumList = category3.stream()
                    .map(ReviewFilterTag::valueOf)
                    .collect(Collectors.toList());

            query.leftJoin(QReview.review)
                    .on(QReview.review.planner.eq(QPlanner.planner))
                    .leftJoin(QReviewTag.reviewTag)
                    .on(QReview.review.id.eq(QReviewTag.reviewTag.review.id))
                    .where(QReviewTag.reviewTag.filterTag.in(category3EnumList));
        }

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            try {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
                query.where(withinRadius(QCourse.course.startLatitude, QCourse.course.startLongitude, lat, lon));

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("위경도 오류");
            }
        }

        if (title != null && !title.isEmpty()) {
            query.where(QCourse.course.title.containsIgnoreCase(title));
        }

        List<SearchListResponse> results = convertToPlannerResponse(query.fetch());
        return PaginationUtil.listToPage(results, pageable);
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