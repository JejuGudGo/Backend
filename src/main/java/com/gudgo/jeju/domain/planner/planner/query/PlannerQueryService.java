package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerDetailResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlannerTag;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotPositionResponse;
import com.gudgo.jeju.domain.planner.spot.entity.QSpot;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerQueryService {
    private final JPAQueryFactory queryFactory;
    private final ReviewQueryService reviewQueryService;

    @Autowired
    public PlannerQueryService(EntityManager entityManager, ReviewQueryService reviewQueryService, PlannerRepository plannerRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.reviewQueryService = reviewQueryService;
    }

    public PlannerDetailResponse getUserPlannerDetail(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        QSpot qSpot = QSpot.spot;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();

        Long courseId = planner.getCourse().getId();

        List<Spot> spots = queryFactory
                .selectFrom(qSpot)
                .where(qSpot.course.id.eq(courseId))
                .fetch();

        List<SpotPositionResponse> spotResponses = spots.stream()
                .map(spot -> new SpotPositionResponse(
                        spot.getOrderNumber(),
                        spot.getTitle(),
                        spot.getLatitude(),
                        spot.getLongitude(),
                        "None"
                ))
                .toList();

        List<PlannerType> tags = queryFactory
                .select(qPlannerTag.code)
                .from(qPlanner)
                .where(qPlannerTag.planner.id.eq(plannerId))
                .fetch();

        Long reviewCount = reviewQueryService.getUserCourseReviewCount(plannerId);

        return new PlannerDetailResponse(
                planner.getCourse().getTitle(),
                planner.getSummary(),
                planner.getCourse().getTotalDistance(),
                planner.getTime(),
                planner.getCourse().getStarAvg(),
                reviewCount,
                tags,
                spotResponses
        );
    }
}
