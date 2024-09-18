package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.QJeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
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
import com.gudgo.jeju.domain.review.entity.QReview;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlannerQueryService {
    private final JPAQueryFactory queryFactory;
    private final ReviewQueryService reviewQueryService;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;

    @Autowired
    public PlannerQueryService(EntityManager entityManager, ReviewQueryService reviewQueryService, PlannerRepository plannerRepository, JeJuOlleCourseRepository jeJuOlleCourseRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.reviewQueryService = reviewQueryService;
        this.jeJuOlleCourseRepository = jeJuOlleCourseRepository;
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

    public List<PlannerListResponse> getUserCreatedPlanners(Long userId) {
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.course.originalCourseId.eq(qPlanner.course.id)
                        .and(qPlanner.course.originalCreatorId.eq(userId)))
                .fetch();

        return convertPlannersToResponses(planners);
    }

    public List<PlannerListResponse> getUserCompletedPlanners(Long userId) {
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.user.id.eq(userId)
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue()))
                .fetch();

//        List<Planner> planners = queryFactory
//                .selectFrom(qPlanner)
//                .where(qPlanner.user.id.eq(userId)
//                        .and(qPlanner.isDeleted.isFalse()))
//                .fetch();

        return convertPlannersToResponses(planners);
    }

    private List<PlannerListResponse> convertPlannersToResponses(List<Planner> planners) {
        return planners.stream()
                .map(this::convertPlannerToResponse)
                .collect(Collectors.toList());
    }

    private PlannerListResponse convertPlannerToResponse(Planner planner) {
        Course course = planner.getCourse();
        String distance = getDistance(course);
        Long reviewCount = reviewQueryService.getUserCourseReviewCount(planner.getId());
        List<String> tagResponses = getPlannerTags(planner);

        return new PlannerListResponse(
                planner.getId(),
                course.getContent(),
                distance,
                planner.getTime(),
                course.getStarAvg(),
                reviewCount,
                planner.isCompleted(),
                planner.isPrivate(),
                tagResponses
        );
    }

    private String getDistance(Course course) {
        if (course.getType().equals(CourseType.JEJU) || course.getType().equals(CourseType.HAYOUNG)) {
            JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(course.getId())
                    .orElseThrow(EntityNotFoundException::new);
            return jeJuOlleCourse.getTotalDistance();
        }
        return null;
    }

    private List<String> getPlannerTags(Planner planner) {
        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        return queryFactory
                .select(qPlannerTag.code.stringValue())
                .from(qPlannerTag)
                .where(qPlannerTag.planner.eq(planner))
                .fetch();
    }
}
