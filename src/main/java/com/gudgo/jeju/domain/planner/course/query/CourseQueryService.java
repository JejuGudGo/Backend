package com.gudgo.jeju.domain.planner.course.query;


import com.gudgo.jeju.domain.olle.entity.JeJuOlleSpot;
import com.gudgo.jeju.domain.olle.entity.QJeJuOlleSpot;
import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.spot.entity.QSpot;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 코스 상세
    public CourseResponseDto getCourse(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;
        QSpot qSpot = QSpot.spot;

        Course course = queryFactory
                .select(qPlanner.course)
                .from(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();

        List<Spot> spots = queryFactory
                .select(qSpot)
                .from(qSpot)
                .join(qSpot.course, qCourse)
                .where(qCourse.id.eq(course.getId())
                        .and(qSpot.isDeleted.isFalse()))
                .fetch();

        List<SpotResponseDto> spotResponses = spots.stream()
                .map(spot ->
                        new SpotResponseDto(
                                spot.getId(),
                                spot.getTourApiCategory1().getId(),
                                spot.getCourse().getId(),
                                spot.getTitle(),
                                spot.getOrderNumber(),
                                spot.getAddress(),
                                spot.getLatitude(),
                                spot.getLongitude(),
                                spot.isCompleted(),
                                spot.getCount()
                        )).toList();

        return new CourseResponseDto(
                course.getId(),
                course.getType(),
                course.getTitle(),
                course.getCreatedAt(),
                course.getOriginalCreatorId(),
                course.getOriginalCourseId(),
                null,
                course.getImageUrl(),
                course.getContent(),
                course.getStarAvg(),
                spotResponses
        );
    }
    




    public CourseResponseDto getOlleCourse(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;
        QJeJuOlleSpot qJeJuOlleSpot = QJeJuOlleSpot.jeJuOlleSpot;

        Course course = queryFactory
                .select(qPlanner.course)
                .from(qPlanner)
                .where(qPlanner.id.eq(plannerId))
                .fetchOne();

        List<JeJuOlleSpot> spots = queryFactory
                .selectFrom(qJeJuOlleSpot)
                .where(qJeJuOlleSpot.jeJuOlleCourse.id.eq(course.getOlleCourseId())) // JeJuOlleCourse의 ID로 필터링
                .fetch();

        List<SpotResponseDto> spotResponses = spots.stream()
                .map(spot ->
                        new SpotResponseDto(
                                spot.getId(),
                                null,
                                spot.getJeJuOlleCourse().getId(),
                                spot.getTitle(),
                                spot.getOrderNumber(),
                                null,
                                spot.getLatitude(),
                                spot.getLongitude(),
                                true,
                                null
                        )).toList();

        return new CourseResponseDto(
                course.getId(),
                course.getType(),
                course.getTitle(),
                course.getCreatedAt(),
                course.getOriginalCreatorId(),
                course.getOriginalCourseId(),
                course.getOlleCourseId(),
                null,
                null,
                course.getStarAvg(),
                spotResponses
        );
    }
}