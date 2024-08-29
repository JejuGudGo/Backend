package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.planner.course.query.CourseQueryService;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerResponse;
import com.gudgo.jeju.domain.planner.entity.*;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerQueryService {
    private final JPAQueryFactory queryFactory;
    private final CourseQueryService courseQueryService;

    @Autowired
    public PlannerQueryService(EntityManager entityManager, CourseQueryService courseQueryService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.courseQueryService = courseQueryService;
    }

    public Page<PlannerResponse> getUserPlanners(Pageable pageable) {
        QPlanner qplanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .selectFrom(qplanner)
                .where(qplanner.isDeleted.isFalse()
                        .and(qplanner.isCompleted.isTrue())
                        .and(qplanner.isPrivate.isFalse()))
                .fetch();

        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner ->
                        new PlannerResponse(
                                planner.getId(),
                                planner.getStartAt(),
                                planner.getSummary(),
                                planner.getTime(),
                                planner.isCompleted(),
                                courseQueryService.getCourse(planner.getId())
                        ))
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }

    public Page<PlannerResponse> getMyPlanners(Long userId, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.isDeleted.isFalse()
                        .and(qPlanner.user.id.eq(userId)))
                .fetch();

        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner ->
                        new PlannerResponse(
                                planner.getId(),
                                planner.getStartAt(),
                                planner.getSummary(),
                                planner.getTime(),
                                planner.isCompleted(),
                                courseQueryService.getCourse(planner.getId())
                        ))
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }

    public Page<PlannerResponse> getMyUncompletedPlanners(Long userId, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.isDeleted.isFalse()
                        .and(qPlanner.isCompleted.isFalse())
                        .and(qPlanner.user.id.eq(userId)))
                .fetch();

        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner ->
                        new PlannerResponse(
                                planner.getId(),
                                planner.getStartAt(),
                                planner.getSummary(),
                                planner.getTime(),
                                planner.isCompleted(),
                                courseQueryService.getCourse(planner.getId())
                        ))
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }

    public PlannerResponse getPlanners(Long userId, Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId)
                        .and(qPlanner.user.id.eq(userId)))
                .fetchOne();

        PlannerResponse plannerResponse = new PlannerResponse(
                planner.getId(),
                planner.getStartAt(),
                planner.getSummary(),
                planner.getTime(),
                planner.isCompleted(),
                courseQueryService.getCourse(planner.getId())
        );

        return plannerResponse;
    }
}
