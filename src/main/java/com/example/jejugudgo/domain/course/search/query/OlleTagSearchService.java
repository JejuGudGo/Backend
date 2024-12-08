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
        JPAQuery<OlleCourse> query = queryFactory
                .selectFrom(olleCourse)
                .join(olleSpot)
                .on(olleSpot.olleCourse.eq(olleCourse))
                .where(
                        olleSpot.spotOrder.eq(1L)
                                .and(
                                        olleSpot.latitude.between(
                                                request.coordinate().get(0).latitude(),
                                                request.coordinate().get(1).latitude()
                                        )
                                )
                                .and(
                                        olleSpot.longitude.between(
                                                request.coordinate().get(0).longitude(),
                                                request.coordinate().get(1).longitude()
                                        )
                                )
                );

        List<String> cat2 = request.cat2();
        List<String> cat3 = request.cat3();

        if (cat2 != null && !cat2.isEmpty()) {
            List<OlleType> types = cat2.stream()
                    .map(OlleType::fromType)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .where(
                            olleCourse.olleType.in(types)
                    );
        }

        if (cat3 != null && ! cat3.isEmpty()) {
            List<Category3Type> types = cat3.stream()
                    .map(Category3Type::fromInput)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query.leftJoin(userReview)
                    .on(userReview.reviewType.eq(CourseType.COURSE_TYPE02)
                            .and(userReview.targetId.eq(olleCourse.id)))
                    .leftJoin(userReviewCategory3)
                    .on(userReviewCategory3.review.eq(userReview))
                    .where(userReviewCategory3.title.in(types));
        }

            /*
                [정렬 기준]
                1. 리뷰수 > 별점평균 > 좋아요 > 클릭 수
                2. 최근 항목 클릭 수 (최신성)
             */

        List<OlleCourse> olleCourses = query
                .orderBy(olleCourse.reviewCount.desc().nullsLast())
                .orderBy(olleCourse.starAvg.desc().nullsLast())
                .orderBy(olleCourse.likeCount.desc().nullsLast())
                .orderBy(olleCourse.clickCount.desc().nullsLast())
                .orderBy(olleCourse.upToDate.desc())
                .fetch();

        return olleCourses.stream().distinct().toList();
    }

    @Override
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        List<OlleCourse> olleCourses = (List<OlleCourse>) getCoursesByCategory(request);

        return olleCourses.stream()
                .map(course -> {
                    Long courseId = course.getId();
                    LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1(), courseId);

                    List<OlleTag> courseTags = queryFactory
                            .select(olleCourseTag.title)
                            .from(olleCourseTag)
                            .where(olleCourseTag.olleCourse.eq(course))
                            .fetch();

                    List<String> tags = courseTags.stream()
                            .map(OlleTag::getTag)
                            .toList();

                    // 스팟의 시작점과 끝점 구하는 쿼리
                    List<OlleSpot> spots = queryFactory
                            .selectFrom(olleSpot)
                            .where(olleSpot.olleCourse.eq(course))
                            .orderBy(olleSpot.spotOrder.asc())
                            .fetch();

                    OlleSpot startSpot = spots.isEmpty() ? null : spots.get(0); // 시작점
                    OlleSpot endSpot = spots.isEmpty() ? null : spots.get(spots.size() - 1); // 끝점

                    RoutePoint startPoint = new RoutePoint(
                            startSpot.getTitle(),
                            startSpot.getLatitude(),
                            startSpot.getLongitude()
                    );

                    RoutePoint endPoint = new RoutePoint(
                            endSpot.getTitle(),
                            endSpot.getLatitude(),
                            endSpot.getLongitude()
                    );


                    return new CourseSearchResponse(
                            courseId,
                            request.cat1(),
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
                            course.getLikeCount(),
                            course.getClickCount(),
                            course.getUpToDate(),
                            startPoint,
                            endPoint
                    );
                })
                .toList();
    }
}
