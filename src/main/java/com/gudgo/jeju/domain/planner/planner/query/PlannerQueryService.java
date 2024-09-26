package com.gudgo.jeju.domain.planner.planner.query;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerDetailResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerUserResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlannerTag;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotPositionResponse;
import com.gudgo.jeju.domain.planner.spot.entity.QSpot;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.entity.QProfile;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.gudgo.jeju.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

        if (planner == null || planner.getCourse() == null) {
            throw new EntityNotFoundException("Planner or Course not found for id: " + plannerId);
        }

        Long courseId = planner.getCourse().getId();

        List<Spot> spots = queryFactory
                .selectFrom(qSpot)
                .where(qSpot.course.id.eq(courseId))
                .fetch();

        List<SpotPositionResponse> spotResponses = spots.stream()
                .filter(Objects::nonNull)
                .map(spot -> new SpotPositionResponse(
                        spot.getId(),
                        spot.getOrderNumber(),
                        spot.getTitle(),
                        spot.getLatitude(),
                        spot.getLongitude(),
                        "None"
                ))
                .toList();

        // PlannerTag 쿼리 수정
        List<PlannerType> tags = queryFactory
                .select(qPlannerTag.code)
                .from(qPlannerTag)
                .where(qPlannerTag.planner.id.eq(plannerId))
                .fetch();

        Long reviewCount = reviewQueryService.getUserCourseReviewCount(plannerId);

        return new PlannerDetailResponse(
                plannerId,
                planner.getCourse().getId(),
                planner.getCourse().getTitle(),
                planner.getSummary(),
                planner.getCourse().getTotalDistance(),
                planner.getTime(),
                planner.getCourse().getStarAvg(),
                reviewCount != null ? reviewCount : 0L,
                tags != null ? tags : Collections.emptyList(),
                spotResponses
        );
    }

    public PlannerUserResponse getPlannerUserInfo(Long userId) {
        QPlanner qPlanner = QPlanner.planner;
        QUser qUser = QUser.user;
        QProfile qProfile = QProfile.profile;

        List<Planner> planners = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.user.id.eq(userId)
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue()))
                .fetch();

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.id.eq(userId))
                .fetchOne();

        if (user == null || user.getProfile() == null) {
            throw new EntityNotFoundException("User or Profile not found for id: " + userId);
        }

        Profile profile = queryFactory
                .selectFrom(qProfile)
                .where(qProfile.id.eq(user.getProfile().getId()))
                .fetchOne();

        if (profile == null) {
            throw new EntityNotFoundException("Profile not found for user id: " + userId);
        }

        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(
                userId,
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getNumberTag(),
                profile.getProfileImageUrl(),
                user.getRole()
        );

        return new PlannerUserResponse(
                userInfoResponseDto,
                profile.getWalkingTime(),
                profile.getWalkingCount() != null ? profile.getWalkingCount() : 0,
                profile.getBadgeCount() != null ? profile.getBadgeCount() : 0
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

        return convertPlannersToResponses(planners);
    }

    public List<PlannerListResponse> getTopRatedPlanners() {
        QPlanner qPlanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;

        List<Planner> topPlanners = queryFactory
                .selectFrom(qPlanner)
                .join(qPlanner.course, qCourse)
                .where(qCourse.starAvg.isNotNull())
                .orderBy(qCourse.starAvg.desc())
                .limit(10)
                .fetch();

        return convertPlannersToResponses(topPlanners);
    }

    private List<PlannerListResponse> convertPlannersToResponses(List<Planner> planners) {
        return planners.stream()
                .map(this::convertUserPlannerToResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PlannerListResponse convertUserPlannerToResponse(Planner planner) {
        if (planner == null || planner.getCourse() == null) {
            return null;
        }

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
                reviewCount != null ? reviewCount : 0L,
                planner.isCompleted(),
                planner.isPrivate(),
                tagResponses != null ? tagResponses : Collections.emptyList()
        );
    }

    private String getDistance(Course course) {
        if (course == null || course.getType() == null) {
            return null;
        }

        if (course.getType().equals(CourseType.JEJU) || course.getType().equals(CourseType.HAYOUNG)) {
            try {
                JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(course.getId())
                        .orElseThrow(() -> new EntityNotFoundException("JeJuOlleCourse not found for id: " + course.getId()));
                return jeJuOlleCourse.getTotalDistance();
            } catch (EntityNotFoundException e) {
                // Log the exception
                return null;
            }
        }
        return null;
    }

    private List<String> getPlannerTags(Planner planner) {
        if (planner == null) {
            return Collections.emptyList();
        }

        QPlannerTag qPlannerTag = QPlannerTag.plannerTag;
        List<String> tags = queryFactory
                .select(qPlannerTag.code.stringValue())
                .from(qPlannerTag)
                .where(qPlannerTag.planner.eq(planner))
                .fetch();

        return tags != null ? tags : Collections.emptyList();
    }
}