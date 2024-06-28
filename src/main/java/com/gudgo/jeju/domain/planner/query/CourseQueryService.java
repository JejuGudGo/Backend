package com.gudgo.jeju.domain.planner.query;


import com.gudgo.jeju.domain.planner.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.entity.*;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                spotResponses
        );
    }
}