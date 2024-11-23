package com.example.jejugudgo.domain.search.query;

import com.example.jejugudgo.domain.course.jejugudgo.entity.*;
import com.example.jejugudgo.domain.review.entity.QReview;
import com.example.jejugudgo.domain.review.entity.QReviewCategory;
import com.example.jejugudgo.domain.review.enums.ReviewCategory3;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.component.SpotCalculator;
import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.util.BookmarkUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JejuGudgoSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final SpotCalculator spotCalculator;
    private final BookmarkUtil bookmarkUtil;;
    private final ReviewCounter reviewCounter;

    @Autowired
    public JejuGudgoSearchQueryService(
            EntityManager entityManager, SpotCalculator spotCalculator, BookmarkUtil bookmarkUtil, ReviewCounter reviewCounter
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.spotCalculator = spotCalculator;
        this.bookmarkUtil = bookmarkUtil;
        this.reviewCounter = reviewCounter;
    }

    QJejuGudgoCourse qJejuGudgoCourse = QJejuGudgoCourse.jejuGudgoCourse;
    QJejuGudgoCourseTag qJejuGudgoCourseTag = QJejuGudgoCourseTag.jejuGudgoCourseTag;
    QReview qReview = QReview.review;
    QReviewCategory qReviewCategory = QReviewCategory.reviewCategory;

    public List<SearchListResponse> getJejuGudgoCourses(
            HttpServletRequest request, List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        List<JejuGudgoCourse> courses = findCoursesByCurrentSpotAndCategory(category2, category3, latitude, longitude, pageable);
        List<SearchListResponse> responses = getResponses(request, courses);

        return responses == null ? new ArrayList<>() : responses;
    }

    private List<JejuGudgoCourse> findCoursesByCurrentSpotAndCategory(
            List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        JPAQuery<JejuGudgoCourse> query = queryFactory
                .selectDistinct(qJejuGudgoCourse)
                .from(qJejuGudgoCourse)
                .where(
                        qJejuGudgoCourse.isDeleted.eq(false)
                )
                .groupBy(qJejuGudgoCourse.id);;

        if (pageable.isPaged()) {
            query.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            query
                    .where(
                            spotCalculator.isAroundSpot(
                                    qJejuGudgoCourse.startLatitude,
                                    qJejuGudgoCourse.startLongitude,
                                    latitude,
                                    longitude
                            )
                    );
        }

        if (category2 != null && !category2.isEmpty()) {
            // fromTag  로 category2 에 해당하지 않는 태그는 null 로 반환.
            List<CourseTag> types = category2.stream()
                    .map(CourseTag::fromTag)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .join(qJejuGudgoCourseTag).on(qJejuGudgoCourseTag.jejuGudgoCourse.eq(qJejuGudgoCourse))
                    .where(qJejuGudgoCourseTag.courseTag.in(types));
        }

        if (category3 != null && !category3.isEmpty()) {
            List<ReviewCategory3> types = category3.stream()
                    .map(ReviewCategory3::fromQuery)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .leftJoin(qReview).on(qReview.jejuGudgoCourse.eq(qJejuGudgoCourse))
                    .leftJoin(qReviewCategory).on(qReviewCategory.eq(qReviewCategory))
                    .where(
                            qReviewCategory.category3.in(types)
                    );
        }

        // TODO: 정렬조건 확인
        List<JejuGudgoCourse> jejuGudgoCourses = query
                .orderBy(qJejuGudgoCourse.title.asc())
                .orderBy(qJejuGudgoCourse.id.asc())
                .fetch();

        return jejuGudgoCourses.stream().distinct().toList();
    }

    private List<SearchListResponse> getResponses(HttpServletRequest request, List<JejuGudgoCourse> jejuGudgoCourses) {
        return jejuGudgoCourses.stream()
                .map(course -> {
                    Long courseId = course.getId();

                    List<CourseTag> jejuGudgoCourseTags = queryFactory
                            .select(qJejuGudgoCourseTag.courseTag)
                            .from(qJejuGudgoCourseTag)
                            .where(qJejuGudgoCourseTag.jejuGudgoCourse.eq(course))
                            .fetch();

                    List<String> tags = jejuGudgoCourseTags.stream()
                            .map(CourseTag::getTag)
                            .toList();

                    Bookmark bookmark =  bookmarkUtil
                            .isBookmarked(request, BookmarkType.JEJU_GUDGO, course.getId());

                    Double starAvg = course.getStarAvg();

                    return new SearchListResponse(
                            courseId,
                            BookmarkType.JEJU_GUDGO.getCode(),
                            tags,
                            bookmark != null,
                            bookmark != null ? bookmark.getId() : null,
                            course.getTitle(),
                            course.getStartSpotTitle() + "-" + course.getEndSpotTitle(),
                            null,
                            course.getDistance(),
                            course.getTime(),
                            course.getImageUrl(),
                            starAvg == 0.0 ? null : starAvg,
                            reviewCounter.getReviewCount(BookmarkType.JEJU_GUDGO, courseId),
                            course.getStartSpotTitle(),
                            course.getStartLatitude(),
                            course.getStartLongitude()
                    );
                })
                .collect(Collectors.toList());
    }
}
