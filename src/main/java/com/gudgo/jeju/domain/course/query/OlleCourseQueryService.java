package com.gudgo.jeju.domain.course.query;


import com.gudgo.jeju.domain.course.dto.response.OlleCourseResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.QCourse;
import com.gudgo.jeju.global.data.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.global.data.olle.entity.QJeJuOlleCourse;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OlleCourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OlleCourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<OlleCourseResponseDto> getOlleCourses(Pageable pageable) {
        QJeJuOlleCourse qCourse = QJeJuOlleCourse.jeJuOlleCourse;

        List<JeJuOlleCourse> courses = queryFactory
                .selectFrom(qCourse)
                .fetch();

        List<OlleCourseResponseDto> courseResponseDtos = courses.stream()
                .map(course ->
                        new OlleCourseResponseDto(
                                course.getId(),
                                course.getOlleType(),
                                course.getCourseNumber(),
                                course.getTitle(),
                                course.getStartLatitude(),
                                course.getStartLongitude(),
                                course.getEndLatitude(),
                                course.getEndLongitude(),
                                course.isWheelchairAccessible(),
                                course.getTotalDistance(),
                                course.getTotalTime()
                        ))
                .toList();
        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

}
