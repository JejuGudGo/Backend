package com.example.jejugudgo.domain.search.query;

import com.example.jejugudgo.domain.course.olle.entity.*;
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
public class JejuOlleSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final SpotCalculator spotCalculator;
    private final BookmarkUtil bookmarkUtil;;
    private final ReviewCounter reviewCounter;

    @Autowired
    public JejuOlleSearchQueryService(
            EntityManager entityManager, SpotCalculator spotCalculator, BookmarkUtil bookmarkUtil, ReviewCounter reviewCounter
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.spotCalculator = spotCalculator;
        this.bookmarkUtil = bookmarkUtil;
        this.reviewCounter = reviewCounter;
    }

    QJejuOlleCourse qJejuOlleCourse = QJejuOlleCourse.jejuOlleCourse;
    QJejuOlleCourseTag qJejuOlleCourseTag = QJejuOlleCourseTag.jejuOlleCourseTag;
    QReview qReview = QReview.review;
    QReviewCategory qReviewCategory = QReviewCategory.reviewCategory;

    public List<SearchListResponse> getJejuOlleCourses(
            HttpServletRequest request, List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        List<JejuOlleCourse> courses = findCoursesByCurrentSpotAndCategory(category2, category3, latitude, longitude, pageable);
        List<SearchListResponse> responses = getResponses(request, courses);

        return responses == null ? new ArrayList<>() : responses;
    }

    private List<JejuOlleCourse> findCoursesByCurrentSpotAndCategory(
            List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        JPAQuery<JejuOlleCourse> query = queryFactory
                .selectDistinct(qJejuOlleCourse)
                .from(qJejuOlleCourse);

        if (pageable.isPaged()) {
            query.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            query
                    .where(
                            spotCalculator.isAroundSpot(
                                    qJejuOlleCourse.startLatitude,
                                    qJejuOlleCourse.startLongitude,
                                    latitude,
                                    longitude
                            )
                    );
        }

        if (category2 != null && !category2.isEmpty()) {
            List<OlleType> types = category2.stream()
                    .map(OlleType::fromType)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .where(
                            qJejuOlleCourse.olleType.in(types)
                    );
        }

        if (category3 != null && !category3.isEmpty()) {
            List<ReviewCategory3> types = category3.stream()
                    .map(ReviewCategory3::fromQuery)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .leftJoin(qReview).on(qReview.jejuOlleCourse.eq(qJejuOlleCourse))
                    .leftJoin(qReviewCategory).on(qReviewCategory.eq(qReviewCategory))
                    .where(
                            qReviewCategory.category3
                                    .in(types)
                    );
        }

        // TODO: 정렬조건 확인
        List<JejuOlleCourse> jejuOlleCourses = query
                .orderBy(qJejuOlleCourse.title.asc())
                .orderBy(qJejuOlleCourse.id.asc())
                .fetch();

        return jejuOlleCourses.stream().distinct().toList();
    }

    private List<SearchListResponse> getResponses(HttpServletRequest request, List<JejuOlleCourse> jejuOlleCourses) {
        return jejuOlleCourses.stream()
                .map(course -> {
                    Long courseId = course.getId();
                    Bookmark bookmark = bookmarkUtil.isBookmarked(request, BookmarkType.OLLE, courseId);

                    List<OlleTag> olleCourseTags = queryFactory
                            .select(qJejuOlleCourseTag.olleTag)
                            .from(qJejuOlleCourseTag)
                            .where(qJejuOlleCourseTag.jejuOlleCourse.eq(course))
                            .fetch();

                    List<String> tags = olleCourseTags.stream()
                            .map(OlleTag::getTag)
                            .toList();

                    return new SearchListResponse(
                            courseId,
                            BookmarkType.OLLE.getCode(),
                            tags,
                            bookmark != null,
                            bookmark != null ? bookmark.getId() : null,
                            course.getTitle(),
                            course.getSummary(),
                            course.getDistance(),
                            course.getTime(),
                            course.getCourseImageUrl(),
                            course.getStarAvg(),
                            reviewCounter.getReviewCount(BookmarkType.OLLE, courseId),
                            course.getStartSpotTitle(),
                            course.getStartLatitude(),
                            course.getStartLongitude()
                    );
                })
                .collect(Collectors.toList());
    }
}
