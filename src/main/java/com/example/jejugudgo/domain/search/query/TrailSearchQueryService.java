package com.example.jejugudgo.domain.search.query;

import com.example.jejugudgo.domain.review.entity.QReview;
import com.example.jejugudgo.domain.review.entity.QReviewCategory;
import com.example.jejugudgo.domain.review.enums.ReviewCategory3;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.component.SpotCalculator;
import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.trail.entity.QTrail;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.entity.TrailType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrailSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final SpotCalculator spotCalculator;
    private final BookmarkUtil bookmarkUtil;;
    private final ReviewCounter reviewCounter;

    @Autowired
    public TrailSearchQueryService(
            EntityManager entityManager, SpotCalculator spotCalculator, BookmarkUtil bookmarkUtil, ReviewCounter reviewCounter
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.spotCalculator = spotCalculator;
        this.bookmarkUtil = bookmarkUtil;
        this.reviewCounter = reviewCounter;
    }

    QTrail qTrail = QTrail.trail;
    QReview qReview = QReview.review;
    QReviewCategory qReviewCategory = QReviewCategory.reviewCategory;

    public List<SearchListResponse> getTrails(
            HttpServletRequest request, List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        List<Trail> courses = findTrailsByCurrentSpotAndCategory(category2, category3, latitude, longitude, pageable);
        List<SearchListResponse> responses = getResponses(request, courses);

        return responses == null ? new ArrayList<>() : responses;
    }

    private List<Trail> findTrailsByCurrentSpotAndCategory(
            List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        JPAQuery<Trail> query = queryFactory
                .selectFrom(qTrail);

        if (pageable.isPaged()) {
            query.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            query
                    .where(
                            spotCalculator.isAroundSpot(
                                    qTrail.latitude,
                                    qTrail.longitude,
                                    latitude,
                                    longitude
                            )
                    );
        }

        if (category2 != null && !category2.isEmpty()) {
            List<TrailType> types = category2.stream()
                    .map(TrailType::fromQuery)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .where(qTrail.trailType.in(types));
        }

        if (category3 != null && !category3.isEmpty()) {
            List<ReviewCategory3> types = category3.stream()
                    .map(ReviewCategory3::fromQuery)
                    .filter(Objects::nonNull) // 여러개가 가능하므로 일단 null 이 아닌 태그는 넣어둔다.
                    .toList();

            if (types.isEmpty())
                return new ArrayList<>();

            query
                    .leftJoin(qReview).on(qReview.trail.eq(qTrail))
                    .leftJoin(qReviewCategory).on(qReviewCategory.eq(qReviewCategory))
                    .where(
                            qReviewCategory.category3.in(types)
                    );
        }

        // TODO: 정렬조건 확인
        List<Trail> trails = query
                .orderBy(qTrail.title.asc())
                .orderBy(qTrail.id.asc())
                .fetch();

        return trails;
    }

    private List<SearchListResponse> getResponses(HttpServletRequest request, List<Trail> trails) {
        return trails.stream()
                .map(trail -> {
                    Long trailId = trail.getId();

                    List<String> tags = new ArrayList<>();
                    tags.add(trail.getTrailType().getCode());

                    Bookmark bookmark =  bookmarkUtil
                            .isBookmarked(request, BookmarkType.TRAIL, trail.getId());

                    return new SearchListResponse(
                            trailId,
                            BookmarkType.TRAIL.getCode(),
                            tags,
                            bookmark != null,
                            bookmark != null ? bookmark.getId() : null,
                            trail.getTitle(),
                            trail.getContent(),
                            null,
                            null,
                            trail.getImageUrl(),
                            trail.getStarAvg(),
                            reviewCounter.getReviewCount(BookmarkType.TRAIL, trailId),
                            trail.getTitle(),
                            trail.getLatitude(),
                            trail.getLongitude()
                    );
                })
                .distinct()
                .collect(Collectors.toList());
    }
}
