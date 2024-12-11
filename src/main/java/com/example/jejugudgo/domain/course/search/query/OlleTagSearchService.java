package com.example.jejugudgo.domain.course.search.query;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.entity.*;
import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.common.enums.OlleTag;
import com.example.jejugudgo.domain.course.common.enums.OlleType;
import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.util.UserLikeUtil;
import com.example.jejugudgo.domain.mygudgo.review.entity.QUserReview;
import com.example.jejugudgo.domain.mygudgo.review.entity.QUserReviewCategory3;
import com.example.jejugudgo.domain.mygudgo.review.enums.Category3Type;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OlleTagSearchService implements TagSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final UserLikeUtil userLikeUtil;

    @Autowired
    public OlleTagSearchService(EntityManager entityManager, UserLikeUtil userLikeUtil) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.userLikeUtil = userLikeUtil;
    }

    QOlleCourse olleCourse = QOlleCourse.olleCourse;
    QOlleCourseTag olleCourseTag = QOlleCourseTag.olleCourseTag;
    QOlleSpot olleSpot = QOlleSpot.olleSpot;
    QUserReview userReview = QUserReview.userReview;
    QUserReviewCategory3 userReviewCategory3 = QUserReviewCategory3.userReviewCategory3;

    @Override
    public List<?> getCoursesByCategory(CourseSearchRequest request) {
        System.out.println("Coordinates: " + request.coordinate());

        // 기본 쿼리 설정
        JPAQuery<OlleCourse> query = queryFactory
                .selectFrom(olleCourse)
                .join(olleSpot).on(olleSpot.olleCourse.eq(olleCourse))
                .where(
                        olleSpot.spotOrder.eq(1L)
                                .and(olleSpot.latitude.between(
                                        request.coordinate().get(0).latitude(),
                                        request.coordinate().get(1).latitude()
                                ))
                                .and(olleSpot.longitude.between(
                                        request.coordinate().get(0).longitude(),
                                        request.coordinate().get(1).longitude()
                                ))
                );

        System.out.println("Base Query Executed");

        // Category2 조건 추가
        List<String> cat2 = request.cat2();
        if (cat2 != null && !cat2.isEmpty()) {
            List<OlleType> types = cat2.stream()
                    .map(OlleType::fromType)
                    .filter(Objects::nonNull)
                    .toList();

            if (!types.isEmpty()) {
                query.where(olleCourse.olleType.in(types));
                System.out.println("Category2 Filter Applied: " + types);
            }
        }

        // Category3 조건 추가
        List<String> cat3 = request.cat3();
        if (cat3 != null && !cat3.isEmpty()) {
            List<Category3Type> types = cat3.stream()
                    .map(Category3Type::fromInput)
                    .filter(Objects::nonNull)
                    .toList();

            if (!types.isEmpty()) {
                query.leftJoin(userReview)
                        .on(userReview.reviewType.eq(CourseType.COURSE_TYPE02)
                                .and(userReview.targetId.eq(olleCourse.id)))
                        .leftJoin(userReviewCategory3)
                        .on(userReviewCategory3.review.eq(userReview))
                        .where(userReviewCategory3.title.in(types));
                System.out.println("Category3 Filter Applied: " + types);
            }
        }

        // 정렬 및 페치
        List<OlleCourse> olleCourses = query
                .distinct()
                .orderBy(olleCourse.reviewCount.desc().nullsLast(),
                        olleCourse.starAvg.desc().nullsLast(),
                        olleCourse.likeCount.desc().nullsLast(),
                        olleCourse.clickCount.desc().nullsLast(),
                        olleCourse.upToDate.desc())
                .fetch();

        System.out.println("Total Courses Found: " + olleCourses.size());
        return olleCourses;
    }

    @Override
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        List<OlleCourse> olleCourses = (List<OlleCourse>) getCoursesByCategory(request);

        return olleCourses.stream()
                .map(course -> {
                    Long courseId = course.getId();
                    LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, CourseType.COURSE_TYPE02.getType(), courseId);

                    List<OlleTag> courseTags = queryFactory
                            .select(olleCourseTag.title)
                            .from(olleCourseTag)
                            .where(olleCourseTag.olleCourse.eq(course))
                            .fetch();

                    List<String> tags = courseTags.stream()
                            .map(OlleTag::getTag)
                            .toList();

                    List<OlleSpot> spots = queryFactory
                            .selectFrom(olleSpot)
                            .where(olleSpot.olleCourse.eq(course))
                            .orderBy(olleSpot.spotOrder.asc())
                            .fetch();

                    OlleSpot startSpot = spots.isEmpty() ? null : spots.get(0);
                    OlleSpot endSpot = spots.isEmpty() ? null : spots.get(spots.size() - 1);

                    RoutePoint startPoint = startSpot != null ?
                            new RoutePoint(startSpot.getTitle(), startSpot.getLatitude(), startSpot.getLongitude()) : null;

                    RoutePoint endPoint = endSpot != null ?
                            new RoutePoint(endSpot.getTitle(), endSpot.getLatitude(), endSpot.getLongitude()) : null;

                    return new CourseSearchResponse(
                            courseId,
                            CourseType.COURSE_TYPE02.getType(),
                            tags,
                            likeInfo,
                            course.getTitle(),
                            null,
                            course.getRoute(),
                            course.getSummary(),
                            null,
                            course.getDistance(),
                            course.getTime(),
                            course.getThumbnailUrl(),
                            course.getStarAvg(),
                            course.getReviewCount(),
                            null,
                            null,
                            null,
                            startPoint,
                            endPoint,
                            CourseType.COURSE_TYPE02.getPinKeyType() + courseId
                    );
                })
                .toList();
    }
}