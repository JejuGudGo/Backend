package com.gudgo.jeju.domain.planner.query;


import com.gudgo.jeju.domain.planner.dto.response.CourseResponseDto;
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

    public Page<CourseResponseDto> getUserCourses(Pageable pageable) {
        QPlanner qplanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;
        QSpot qSpot = QSpot.spot;

        List<Planner> planners = queryFactory
                .selectFrom(qplanner)
                .where(qplanner.isDeleted.isFalse()
                        .and(qplanner.isCompleted.isTrue())
                        .and(qplanner.isPrivate.isFalse()))
                .fetch();

        List<Long> courseIds = planners.stream()
                .map(planner -> planner.getCourse().getId())
                .toList();

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.originalCourseId.in(courseIds))
                .fetch();

        List<Spot> spots = queryFactory
                .selectFrom(qSpot)
                .where(qSpot.isDeleted.isFalse()
                        .and(qSpot.course.id.in(courseIds)))
                .fetch();

        Map<Long, List<Spot>> spotByCourseId = spots.stream()
                .collect(Collectors.groupingBy(spot -> spot.getCourse().getId()));

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new CourseResponseDto(
                                course.getId(),
                                course.getType(),
                                course.getTitle(),
                                course.getCreatedAt(),
                                course.getOriginalCreatorId(),
                                course.getOriginalCourseId(),
                                null,
                                spotByCourseId.getOrDefault(course.getId(), List.of())
                        ))
                .toList();


        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

    public Page<CourseResponseDto> getMyCourses(Long userId, Pageable pageable) {
        QPlanner qplanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;
        QSpot qSpot = QSpot.spot;

        List<Planner> planners = queryFactory
                .selectFrom(qplanner)
                .where(qplanner.isDeleted.isFalse()
                        .and(qplanner.user.id.eq(userId)))
                .fetch();

        List<Long> courseIds = planners.stream()
                .map(planner -> planner.getCourse().getId())
                .toList();

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.originalCourseId.in(courseIds))
                .fetch();


        List<Spot> spots = queryFactory
                .selectFrom(qSpot)
                .where(qSpot.isDeleted.isFalse()
                        .and(qSpot.course.id.in(courseIds)))
                .fetch();

        Map<Long, List<Spot>> spotByCourseId = spots.stream()
                .collect(Collectors.groupingBy(spot -> spot.getCourse().getId()));

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new CourseResponseDto(
                                course.getId(),
                                course.getType(),
                                course.getTitle(),
                                course.getCreatedAt(),
                                course.getOriginalCreatorId(),
                                course.getOriginalCourseId(),
                                course.getOlleCourseId(),
                                spotByCourseId.getOrDefault(course.getId(), List.of())
                        ))
                .toList();

        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

    public Page<CourseResponseDto> getMyUnCompletedCourses(Long userId, Pageable pageable) {
        QPlanner qplanner = QPlanner.planner;
        QCourse qCourse = QCourse.course;
        QSpot qSpot = QSpot.spot;

        List<Planner> planners = queryFactory
                .selectFrom(qplanner)
                .where(qplanner.isDeleted.isFalse()
                        .and(qplanner.isCompleted.isFalse())
                        .and(qplanner.user.id.eq(userId)))
                .fetch();

        List<Long> courseIds = planners.stream()
                .map(planner -> planner.getCourse().getId())
                .toList();

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.originalCourseId.in(courseIds))
                .fetch();


        List<Spot> spots = queryFactory
                .selectFrom(qSpot)
                .where(qSpot.isDeleted.isFalse()
                        .and(qSpot.course.id.in(courseIds)))
                .fetch();

        Map<Long, List<Spot>> spotByCourseId = spots.stream()
                .collect(Collectors.groupingBy(spot -> spot.getCourse().getId()));

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new CourseResponseDto(
                                course.getId(),
                                course.getType(),
                                course.getTitle(),
                                course.getCreatedAt(),
                                course.getOriginalCreatorId(),
                                course.getOriginalCourseId(),
                                course.getOlleCourseId(),
                                spotByCourseId.getOrDefault(course.getId(), List.of())
                        ))
                .toList();

        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }
}