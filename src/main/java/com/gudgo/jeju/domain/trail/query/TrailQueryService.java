package com.gudgo.jeju.domain.trail.query;

import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.domain.trail.dto.response.TrailListResponse;
import com.gudgo.jeju.domain.trail.dto.response.TrailRecommendResponse;
import com.gudgo.jeju.domain.trail.entity.*;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrailQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public TrailQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<TrailListResponse> getTrails(TrailType type, Pageable pageable) {
        QTrail trail = QTrail.trail;
        QTrailTag trailTag = QTrailTag.trailTag1;
        QReview review = QReview.review;

        List<Trail> trails = queryFactory
                .select(trail)
                .from(trail)
                .leftJoin(trailTag).on(trailTag.trail.eq(trail))
                .where(typeFilter(type))
                .groupBy(trail.id)
                .orderBy(trail.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<TrailListResponse> trailResponses = trails.stream().map(t -> {
            Double avgStars = queryFactory
                    .select(review.stars.avg())
                    .from(review)
                    .where(review.trail.eq(t))
                    .fetchOne();

            Long reviewCount = queryFactory
                    .select(review.count())
                    .from(review)
                    .where(review.trail.eq(t))
                    .fetchOne();

            List<TrailType> tags = queryFactory
                    .select(trailTag.trailTag)
                    .from(trailTag)
                    .where(trailTag.trail.eq(t))
                    .fetch();

            return new TrailListResponse(
                    t.getTitle(),
                    t.getSummary(),
                    avgStars != null ? avgStars : 0.0,
                    reviewCount,
                    tags
            );
        }).collect(Collectors.toList());

        return PaginationUtil.listToPage(trailResponses, pageable);
    }

    private BooleanExpression typeFilter(TrailType type) {
        QTrailTag trailTag = QTrailTag.trailTag1;
        if (type == TrailType.ALL) {
            return null;
        }
        return trailTag.trailTag.eq(type);
    }

    public List<TrailRecommendResponse> findTop5TrailsByMatchingTags(Long trailId) {
        QTrailTag qTrailTag = QTrailTag.trailTag1;

        List<TrailType> trailTags = queryFactory
                .select(qTrailTag.trailTag)
                .from(qTrailTag)
                .where(qTrailTag.trail.id.eq(trailId))
                .fetch();

        List<Trail> trails = queryFactory
                .select(qTrailTag.trail)
                .from(qTrailTag)
                .where(qTrailTag.trailTag.in(trailTags)
                        .and(qTrailTag.trail.id.ne(trailId)))
                .groupBy(qTrailTag.trail)
                .orderBy(qTrailTag.trail.count().desc())
                .limit(5)
                .fetch();

        List<TrailRecommendResponse> responses = trails.stream()
                .map(trail -> {
                    QTrailImage qTrailImage = QTrailImage.trailImage;

                    String imageUrl = queryFactory
                            .select(qTrailImage.imageUrl)
                            .from(qTrailImage)
                            .where(qTrailImage.trail.id.eq(trail.getId()))
                            .fetchOne();

                    return new TrailRecommendResponse(
                            trail.getId(),
                            trail.getTitle(),
                            imageUrl
                    );
                }).toList();

        return responses;
    }

    public List<String> findTrailImages(Long trailId) {
        QTrailImage qTrailImage = QTrailImage.trailImage;

        List<String> trailImages = queryFactory
                .select(qTrailImage.imageUrl)
                .from(qTrailImage)
                .where(qTrailImage.trail.id.eq(trailId))
                .fetch();

        return trailImages;
    }
}
