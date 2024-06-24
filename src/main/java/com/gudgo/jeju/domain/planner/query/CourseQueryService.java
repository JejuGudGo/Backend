package com.gudgo.jeju.domain.planner.query;


import com.gudgo.jeju.domain.planner.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.CourseType;
import com.gudgo.jeju.domain.planner.entity.QCourse;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<CourseResponseDto> getUserCourses(Pageable pageable) {
        QCourse qCourse = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.isDeleted.isFalse()
                        .and(qCourse.isCompleted.isTrue())
                        .and(qCourse.id.eq(qCourse.originalCourseId))
                        .and(qCourse.type.eq(CourseType.USER)))
                .fetch();

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                    new CourseResponseDto(
                            course.getId(),
                            course.getTitle(),
                            course.getCreatedAt(),
                            course.getOriginalCreatorId(),
                            course.getOriginalCourseId(),
                            course.s
                    ))
                .toList();

        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

    public Page<CourseResponseDto> getMyCourses(Long userId, Pageable pageable) {
        QCourse qCourse = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.isDeleted.isFalse()
                        .and(qCourse.user.id.eq(userId))
                        .and(qCourse.type.eq(CourseType.USER)))
                .fetch();

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new CourseResponseDto(
                                course.getId(),
                                course.getTitle(),
                                course.getCreatedAt(),
                                course.getOriginalCreatorId(),
                                course.getOriginalCourseId(),
                        ))
                .toList();

        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

    public Page<CourseResponseDto> getMyUnCompletedCourses(Long userId, Pageable pageable) {
        QCourse qCourse = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.isDeleted.isFalse()
                        .and(qCourse.originalCreatorId.eq(userId))
                        .and(qCourse.isCompleted.isFalse()))
                .fetch();

        List<CourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new CourseResponseDto(
                                course.getId(),
                                course.getTitle(),
                                course.getTime(),
                                course.getStartAt(),
                                course.getCreatedAt(),
                                course.isDeleted(),
                                course.getOriginalCreatorId(),
                                course.getOriginalCourseId(),
                                course.getSummary()
                        ))
                .toList();

        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }
}
