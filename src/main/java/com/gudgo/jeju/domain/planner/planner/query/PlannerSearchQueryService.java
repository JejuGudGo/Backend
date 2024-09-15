package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.planner.course.query.CourseQueryService;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerSearchResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerTagResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.review.entity.QPlannerReview;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerTag;
import com.gudgo.jeju.domain.planner.tag.entity.QPlannerTag;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerSearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final CourseQueryService courseQueryService;

    @Autowired
    public PlannerSearchQueryService(EntityManager entityManager, CourseQueryService courseQueryService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.courseQueryService = courseQueryService;
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

                    QPlannerReview qPlannerReview = QPlannerReview.plannerReview;

                    Long reviewCount = queryFactory
                            .select(qPlannerReview.count())
                            .from(qPlannerReview)
                            .where(qPlannerReview.planner.id.eq(planner.getId()))
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

//
//    public Page<PlannerResponse> getPlannerdetail(Pageable pageable) {
//        QCourse qCourse = QCourse.course;
//        QPlanner qPlanner = QPlanner.planner;
//        QPlannerTag qPlannerTa = QPlannerTag.plannerTag;
//
//        List<Long> userCourseIds = queryFactory
//                .select(qCourse.id)
//                .from(qCourse)
//                .where(qCourse.originalCourseId.eq(qCourse.id)
//                        .and(qCourse.type.eq(CourseType.USER)))
//                .fetch();
//
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.course.id.in(userCourseIds)
//                        .and(qPlanner.isPrivate.isFalse())
//                        .and(qPlanner.isDeleted.isFalse())
//                        .and(qPlanner.isCompleted.isTrue()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<Long> plannerIds = planners.stream().map(Planner::getId).collect(Collectors.toList());
//
//        Map<Long, String> plannerLabelMap = queryFactory
//                .select(qPlannerLabel.planner.id, qPlannerLabel.code)
//                .from(qPlannerLabel)
//                .where(qPlannerLabel.planner.id.in(plannerIds))
//                .fetch()
//                .stream()
//                .collect(Collectors.toMap(
//                        tuple -> tuple.get(qPlannerLabel.planner.id),
//                        tuple -> tuple.get(qPlannerLabel.code)
//                ));
//
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner -> {
//                    String labelCode = plannerLabelMap.get(planner.getId());
//                    return new PlannerResponse(
//                            planner.getId(),
//                            planner.getStartAt(),
//                            planner.getSummary(),
//                            planner.getTime(),
//                            planner.isCompleted(),
//                            labelCode,
//                            courseQueryService.getCourse(planner.getCourse().getId())
//                    );
//                })
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
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
//
//    public Page<PlannerResponse> getOllePlanners(Pageable pageable) {
//        QCourse qCourse = QCourse.course;
//        QPlanner qPlanner = QPlanner.planner;
//
//        // 1. Course 테이블에서 조건에 맞는 데이터 조회
//        List<Long> olleCourseIds = queryFactory
//                .select(qCourse.id)
//                .from(qCourse)
//                .where(qCourse.olleCourseId.eq(qCourse.id))
//                .fetch();
//
//        // 2. Planner 테이블에서 조건에 맞는 데이터 조회
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.course.id.in(olleCourseIds)
//                        .and(qPlanner.isPrivate.isFalse())
//                        .and(qPlanner.isDeleted.isFalse())
//                        .and(qPlanner.isCompleted.isTrue()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // 3. PlannerResponse로 변환
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner ->
//                        new PlannerResponse(
//                                planner.getId(),
//                                planner.getStartAt(),
//                                planner.getSummary(),
//                                planner.getTime(),
//                                planner.isCompleted(),
//                                null,
//                                courseQueryService.getOlleCourse(planner.getCourse().getId())
//                        ))
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
//
//    public Page<PlannerResponse> getAllPlanners(Pageable pageable) {
//        QCourse qCourse = QCourse.course;
//        QPlanner qPlanner = QPlanner.planner;
//        QPlannerLabel qPlannerLabel = QPlannerLabel.plannerLabel;
//
//        // 1. Course 테이블에서 Olle 코스와 사용자 생성 코스 ID 조회
//        List<Long> courseIds = queryFactory
//                .select(qCourse.id)
//                .from(qCourse)
//                .where(qCourse.olleCourseId.eq(qCourse.id)
//                        .or(qCourse.originalCourseId.eq(qCourse.id).and(qCourse.type.eq(CourseType.USER))))
//                .fetch();
//
//        // 2. Planner 테이블에서 조건에 맞는 데이터 조회
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.course.id.in(courseIds)
//                        .and(qPlanner.isPrivate.isFalse())
//                        .and(qPlanner.isDeleted.isFalse())
//                        .and(qPlanner.isCompleted.isTrue()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(qPlanner.startAt.desc())  // 최신 플래너부터 정렬
//                .fetch();
//
//        // 3. PlannerLabel 테이블에서 labelCode 조회
//        List<Long> plannerIds = planners.stream().map(Planner::getId).collect(Collectors.toList());
//        Map<Long, String> labelCodeMap = queryFactory
//                .select(qPlannerLabel.planner.id, qPlannerLabel.code)
//                .from(qPlannerLabel)
//                .where(qPlannerLabel.planner.id.in(plannerIds))
//                .fetch()
//                .stream()
//                .collect(Collectors.toMap(
//                        tuple -> tuple.get(qPlannerLabel.planner.id),
//                        tuple -> tuple.get(qPlannerLabel.code),
//                        (existing, replacement) -> existing  // In case of duplicates, keep the existing value
//                ));
//
//        // 4. PlannerResponse로 변환
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner -> {
//                    CourseResponseDto courseResponseDto;
//                    Course course = planner.getCourse();
//
//                    if (course == null) {
//                        throw new IllegalStateException("Planner with ID " + planner.getId() + " has no associated course");
//                    }
//
//                    if (course.getOlleCourseId() != null && course.getOlleCourseId().equals(course.getId())) {
//                        courseResponseDto = courseQueryService.getOlleCourse(course.getId());
//                    } else if (course.getOlleCourseId() == null && course.getId().equals(course.getOriginalCourseId())) {
//                        courseResponseDto = courseQueryService.getCourse(course.getId());
//                    } else {
//                        // 예상치 못한 경우에 대한 처리
//                        courseResponseDto = courseQueryService.getCourse(course.getId()); // 기본적으로 getCourse 사용
//                    }
//
//                    String labelCode = labelCodeMap.get(planner.getId());  // labelCode가 없으면 null을 반환
//
//                    return new PlannerResponse(
//                            planner.getId(),
//                            planner.getStartAt(),
//                            planner.getSummary(),
//                            planner.getTime(),
//                            planner.isCompleted(),
//                            labelCode,
//                            courseResponseDto
//                    );
//                })
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
//
//    public Page<PlannerResponse> getPlannersByLabel(Pageable pageable, LabelRequestDto requestDto) {
//        QPlanner qPlanner = QPlanner.planner;
//        QPlannerLabel qPlannerLabel = QPlannerLabel.plannerLabel;
//
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlannerLabel.planner)
//                .where(qPlannerLabel.code.eq(requestDto.code()))
//                .fetch();
//
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner -> {
//                    String labelCode = requestDto.code();
//                    return new PlannerResponse(
//                            planner.getId(),
//                            planner.getStartAt(),
//                            planner.getSummary(),
//                            planner.getTime(),
//                            planner.isCompleted(),
//                            labelCode,
//                            courseQueryService.getCourse(planner.getCourse().getId())
//                    );
//                })
//                .toList();
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
//    public Page<PlannerResponse> getMyPlanners(Long userId, Pageable pageable) {
//        QPlanner qPlanner = QPlanner.planner;
//        QPlannerLabel qPlannerLabel = QPlannerLabel.plannerLabel;
//
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.isDeleted.isFalse()
//                        .and(qPlanner.user.id.eq(userId)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<Long> plannerIds = planners.stream().map(Planner::getId).collect(Collectors.toList());
//
//        // PlannerLabel 테이블에서 labelCode 조회
//        Map<Long, String> labelCodeMap = queryFactory
//                .select(qPlannerLabel.planner.id, qPlannerLabel.code)
//                .from(qPlannerLabel)
//                .where(qPlannerLabel.planner.id.in(plannerIds))
//                .fetch()
//                .stream()
//                .collect(Collectors.toMap(
//                        tuple -> tuple.get(qPlannerLabel.planner.id),
//                        tuple -> tuple.get(qPlannerLabel.code),
//                        (existing, replacement) -> existing  // In case of duplicates, keep the existing value
//                ));
//
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner -> {
//                    String labelCode = labelCodeMap.get(planner.getId());  // labelCode가 없으면 null을 반환
//                    return new PlannerResponse(
//                            planner.getId(),
//                            planner.getStartAt(),
//                            planner.getSummary(),
//                            planner.getTime(),
//                            planner.isCompleted(),
//                            labelCode,
//                            courseQueryService.getCourse(planner.getId())
//
//                    );
//                })
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
//    public Page<PlannerResponse> getMyUncompletedPlanners(Long userId, Pageable pageable) {
//        QPlanner qPlanner = QPlanner.planner;
//        QPlannerLabel qPlannerLabel = QPlannerLabel.plannerLabel;
//
//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.isDeleted.isFalse()
//                        .and(qPlanner.isCompleted.isFalse())
//                        .and(qPlanner.user.id.eq(userId)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<Long> plannerIds = planners.stream().map(Planner::getId).collect(Collectors.toList());
//
//        // PlannerLabel 테이블에서 labelCode 조회
//        Map<Long, String> labelCodeMap = queryFactory
//                .select(qPlannerLabel.planner.id, qPlannerLabel.code)
//                .from(qPlannerLabel)
//                .where(qPlannerLabel.planner.id.in(plannerIds))
//                .fetch()
//                .stream()
//                .collect(Collectors.toMap(
//                        tuple -> tuple.get(qPlannerLabel.planner.id),
//                        tuple -> tuple.get(qPlannerLabel.code),
//                        (existing, replacement) -> existing  // In case of duplicates, keep the existing value
//                ));
//
//        List<PlannerResponse> plannerResponses = planners.stream()
//                .map(planner -> {
//                    String labelCode = labelCodeMap.get(planner.getId());  // labelCode가 없으면 null을 반환
//                    return new PlannerResponse(
//                            planner.getId(),
//                            planner.getStartAt(),
//                            planner.getSummary(),
//                            planner.getTime(),
//                            planner.isCompleted(),
//                            labelCode,
//                            courseQueryService.getCourse(planner.getId())
//                            );
//                })
//                .toList();
//
//        return PaginationUtil.listToPage(plannerResponses, pageable);
//    }
//
//    public PlannerResponse getPlanners(Long userId, Long plannerId) {
//        QPlanner qPlanner = QPlanner.planner;
//
//        Planner planner = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.id.eq(plannerId)
//                        .and(qPlanner.user.id.eq(userId)))
//                .fetchOne();
//
//        return createUserPlannerResponse(planner);
//    }
//
//    public PlannerResponse getUserPlanner(Long plannerId) {
//        QPlanner qPlanner = QPlanner.planner;
//
//        Planner planner = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.id.eq(plannerId))
//                .fetchOne();
//
//        return createUserPlannerResponse(planner);
//    }
//
//    public PlannerResponse getOllePlanner(Long plannerId) {
//        QPlanner qPlanner = QPlanner.planner;
//
//        Planner planner = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.id.eq(plannerId))
//                .fetchOne();
//        return new PlannerResponse(
//                planner.getId(),
//                null,
//                null,
//                null,
//                planner.isCompleted(),
//                null,
//                courseQueryService.getOlleCourse(planner.getId())
//        );
//    }
//
//    private PlannerResponse createUserPlannerResponse(Planner planner) {
//        QPlannerLabel qPlannerLabel = QPlannerLabel.plannerLabel;
//        String labelCode = queryFactory
//                .select(qPlannerLabel.code)
//                .from(qPlannerLabel)
//                .where(qPlannerLabel.planner.id.eq(planner.getId()))
//                .fetchOne();
//
//        return new PlannerResponse(
//                planner.getId(),
//                planner.getStartAt(),
//                planner.getSummary(),
//                planner.getTime(),
//                planner.isCompleted(),
//                labelCode,
//                courseQueryService.getCourse(planner.getId())
//        );
//    }
}