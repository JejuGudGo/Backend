package com.example.jejugudgo.domain.course.search.query;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.entity.QTrail;
import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.common.enums.TrailTag;
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
public class TrailTagSearchService implements TagSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final UserLikeUtil userLikeUtil;

    @Autowired
    public TrailTagSearchService(EntityManager entityManager, UserLikeUtil userLikeUtil) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.userLikeUtil = userLikeUtil;
    }

    QTrail trail = QTrail.trail;
    QUserReview userReview = QUserReview.userReview;
    QUserReviewCategory3 userReviewCategory3 = QUserReviewCategory3.userReviewCategory3;

    @Override
    public List getCoursesByCategory(CourseSearchRequest request) {
        JPAQuery<Trail> query = queryFactory
                .selectFrom(trail)
                .where(
                        trail.latitude.between(
                                        request.coordinate().get(0).latitude(),
                                        request.coordinate().get(1).latitude()
                        ).and(
                                trail.longitude.between(
                                        request.coordinate().get(0).longitude(),
                                        request.coordinate().get(1).longitude()
                                )
                        )
                );

        List<String> cat2 = request.cat2();
        List<String> cat3 = request.cat3();

        if (cat2 != null && !cat2.isEmpty()) {
            List<TrailTag> types = cat2.stream()
                    .map(TrailTag::fromInput)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .where(
                            trail.trailTag.in(types)
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
                    .on(userReview.reviewType.eq(CourseType.COURSE_TYPE03)
                            .and(userReview.targetId.eq(trail.id)))
                    .leftJoin(userReviewCategory3)
                    .on(userReviewCategory3.review.eq(userReview))
                    .where(userReviewCategory3.title.in(types));
        }

        /*
                [정렬 기준]
                1. 리뷰수 > 별점평균 > 좋아요 > 클릭 수
                2. 최근 항목 클릭 수 (최신성)
             */

        List<Trail> trails = query
                .orderBy(trail.reviewCount.desc().nullsLast())
                .orderBy(trail.starAvg.desc().nullsLast())
                .orderBy(trail.likeCount.desc().nullsLast())
                .orderBy(trail.clickCount.desc().nullsLast())
                .orderBy(trail.upToDate.desc())
                .fetch();

        return trails.stream().distinct().toList();
    }

    @Override
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        List<Trail> trails = (List<Trail>) getCoursesByCategory(request);

        return trails.stream()
                .map(trail -> {
                    Long trailId = trail.getId();
                    LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1(), trailId);

                    List<String> trailTags = new ArrayList<>();
                    trailTags.add(trail.getTrailTag().getTag());

                    RoutePoint startPoint = new RoutePoint(
                            trail.getTitle(),
                            trail.getLatitude(),
                            trail.getLongitude()
                    );

                    return new CourseSearchResponse(
                            trailId,
                            request.cat1(),
                            trailTags,
                            likeInfo,
                            trail.getTitle(),
                            trail.getAddress(),
                            null,
                            null,
                            trail.getContent(),
                            null,
                            trail.getTime(),
                            trail.getThumbnailUrl(),
                            trail.getStarAvg(),
                            trail.getReviewCount(),
                            null,
                            null,
                            null,
                            startPoint,
                            null,
                            "trail" + trailId
                    );
                }).toList();
    }
}
