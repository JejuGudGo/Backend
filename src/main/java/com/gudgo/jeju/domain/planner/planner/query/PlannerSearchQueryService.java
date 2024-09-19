package com.gudgo.jeju.domain.planner.planner.query;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerSearchResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerTagResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerTag;
import com.gudgo.jeju.domain.planner.planner.entity.QPlannerTag;
import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlannerSearchQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PlannerSearchQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<PlannerSearchResponse> searchPlannersByTitle(Pageable pageable, String title) {
        QPlanner qPlanner = QPlanner.planner;
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.course.title.like("%" + title + "%"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PlannerSearchResponse> plannerResponses = planners.stream()
                .map(planner -> {
                    List<PlannerTag> plannerTags = queryFactory
                            .selectFrom(qPlannerTag)
                            .where(qPlannerTag.planner.id.in(planner.getId()))
                            .fetch();

                    List<PlannerTagResponse> plannerTagResponses = plannerTags.stream()
                            .map(plannerTag -> new PlannerTagResponse(plannerTag.getId(), plannerTag.getCode()))
                            .toList();

                    QReview qReview = QReview.review;

                    Long reviewCount = queryFactory
                            .select(qReview.count())
                            .from(qReview)
                            .where(qReview.planner.id.eq(planner.getId()))
                            .fetchOne();

                    return new PlannerSearchResponse(
                            planner.getId(),
                            planner.getCourse().getTitle(),
                            planner.getCourse().getContent(),
                            "None",
                            planner.getTime(),
                            reviewCount,
                            planner.getCourse().getStarAvg(),
                            plannerTagResponses
                    );
                })
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }

    public int getUserPlannersCount(Long userId) {
        QPlanner qplanner = QPlanner.planner;

        return queryFactory
                .select(qplanner.count())
                .from(qplanner)
                .where(qplanner.user.id.eq(userId)
                        .and(qplanner.course.type.eq(CourseType.USER))
                        .and(qplanner.isDeleted.isFalse())
                        .and(qplanner.isCompleted.isTrue()))
                .fetchOne()
                .intValue();
    }

    public int getOllePlannersCount(Long userId) {
        QPlanner qplanner = QPlanner.planner;

        return queryFactory
                .select(qplanner.count())
                .from(qplanner)
                .where(qplanner.user.id.eq(userId)
                        .and(qplanner.course.type.in(CourseType.JEJU, CourseType.HAYOUNG))
                        .and(qplanner.isDeleted.isFalse())
                        .and(qplanner.isCompleted.isTrue()))
                .fetchOne()
                .intValue();
    }

    public List<LocalDate> getStartAt(Long userId) {
        QPlanner qPlanner = QPlanner.planner;

        return queryFactory
                .select(qPlanner.startAt)
                .from(qPlanner)
                .where(qPlanner.user.id.eq(userId)
                        .and(qPlanner.isCompleted.isTrue())
                        .and(qPlanner.isDeleted.isFalse()))
                .orderBy(qPlanner.startAt.asc())
                .fetch();
    }
}