package com.gudgo.jeju.domain.course.query;


import com.gudgo.jeju.domain.course.dto.response.UserCourseResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.CourseType;
import com.gudgo.jeju.domain.course.entity.QCourse;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public UserCourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<UserCourseResponseDto> getUserCourses(Pageable pageable) {
        QCourse qCourse = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.isDeleted.isFalse()
                        .and(qCourse.id.eq(qCourse.originalCourseId))
                        .and(qCourse.type.eq(CourseType.USER)))
                .fetch();

        List<UserCourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                    new UserCourseResponseDto(
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
