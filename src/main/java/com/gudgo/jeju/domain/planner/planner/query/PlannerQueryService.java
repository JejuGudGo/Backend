package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.course.query.CourseQueryService;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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
        QCourse qCourse = QCourse.course;
        QPlanner qPlanner = QPlanner.planner;

        List<Long> userCourseIds = queryFactory
                .select(qCourse.id)
                .from(qCourse)
                .where(qCourse.originalCourseId.eq(qCourse.id)
                        .and(qCourse.type.eq(CourseType.USER)))
                .fetch();

        // 2. Planner 테이블에서 조건에 맞는 데이터 조회
        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.course.id.in(userCourseIds)
                        .and(qPlanner.isPrivate.isFalse())
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 3. PlannerResponse로 변환
        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner ->
                        new PlannerResponse(
                                planner.getId(),
                                planner.getStartAt(),
                                planner.getSummary(),
                                planner.getTime(),
                                planner.isCompleted(),
                                courseQueryService.getCourse(planner.getCourse().getId())
                        ))
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }

//    public Page<PlannerResponse> getUserPlanners(Pageable pageable) {
//        QPlanner qplanner = QPlanner.planner;
//
//        List<Planner> planners = queryFactory
//                .selectFrom(qplanner)
//                .where(qplanner.isDeleted.isFalse()
//                        .and(qplanner.isCompleted.isTrue())
//                        .and(qplanner.isPrivate.isFalse()))
//                .fetch();
//
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner ->
//                        new PlannerResponse(
//                                planner.getId(),
//                                planner.getStartAt(),
//                                planner.getSummary(),
//                                planner.getTime(),
//                                planner.isCompleted(),
//                                courseQueryService.getCourse(planner.getId())
//                        ))
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }

    public Page<PlannerResponse> getOllePlanners(Pageable pageable) {
        QCourse qCourse = QCourse.course;
        QPlanner qPlanner = QPlanner.planner;

        // 1. Course 테이블에서 조건에 맞는 데이터 조회
        List<Long> olleCourseIds = queryFactory
                .select(qCourse.id)
                .from(qCourse)
                .where(qCourse.olleCourseId.eq(qCourse.id))
                .fetch();

        // 2. Planner 테이블에서 조건에 맞는 데이터 조회
        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.course.id.in(olleCourseIds)
                        .and(qPlanner.isPrivate.isFalse())
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 3. PlannerResponse로 변환
        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner ->
                        new PlannerResponse(
                                planner.getId(),
                                planner.getStartAt(),
                                planner.getSummary(),
                                planner.getTime(),
                                planner.isCompleted(),
                                courseQueryService.getOlleCourse(planner.getCourse().getId())
                        ))
                .toList();

        return PaginationUtil.listToPage(plannerResponses, pageable);
    }


    public Page<PlannerResponse> getAllPlanners(Pageable pageable) {
        QCourse qCourse = QCourse.course;
        QPlanner qPlanner = QPlanner.planner;

        // 1. Course 테이블에서 Olle 코스와 사용자 생성 코스 ID 조회
        List<Long> courseIds = queryFactory
                .select(qCourse.id)
                .from(qCourse)
                .where(qCourse.olleCourseId.eq(qCourse.id)
                        .or(qCourse.originalCourseId.eq(qCourse.id).and(qCourse.type.eq(CourseType.USER))))
                .fetch();

        // 2. Planner 테이블에서 조건에 맞는 데이터 조회
        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.course.id.in(courseIds)
                        .and(qPlanner.isPrivate.isFalse())
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPlanner.startAt.desc())  // 최신 플래너부터 정렬
                .fetch();

        // 3. PlannerResponse로 변환
        List<PlannerResponse> plannerResponses = planners.stream()
                .map(planner -> {
                    CourseResponseDto courseResponseDto;
                    Course course = planner.getCourse();

                    if (course == null) {
                        throw new IllegalStateException("Planner with ID " + planner.getId() + " has no associated course");
                    }

                    if (course.getOlleCourseId() != null && course.getOlleCourseId().equals(course.getId())) {
                        courseResponseDto = courseQueryService.getOlleCourse(course.getId());
                    } else if (course.getOlleCourseId() == null && course.getId().equals(course.getOriginalCourseId())) {
                        courseResponseDto = courseQueryService.getCourse(course.getId());
                    } else {
                        // 예상치 못한 경우에 대한 처리
                        courseResponseDto = courseQueryService.getCourse(course.getId()); // 기본적으로 getCourse 사용
                    }

                    return new PlannerResponse(
                            planner.getId(),
                            planner.getStartAt(),
                            planner.getSummary(),
                            planner.getTime(),
                            planner.isCompleted(),
                            courseResponseDto
                    );
                })
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

        return createUserPlannerResponse(planner);
    }

    public PlannerResponse getUserPlanner(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();

        return createUserPlannerResponse(planner);
    }

    public PlannerResponse getOllePlanner(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();
        return new PlannerResponse(
                planner.getId(),
                null,
                null,
                null,
                planner.isCompleted(),
                courseQueryService.getOlleCourse(planner.getId())
        );
    }

    private PlannerResponse createUserPlannerResponse(Planner planner) {
        return new PlannerResponse(
                planner.getId(),
                planner.getStartAt(),
                planner.getSummary(),
                planner.getTime(),
                planner.isCompleted(),
                courseQueryService.getCourse(planner.getId())
        );
    }
}
