package com.gudgo.jeju.domain.planner.courseMedia.query;

import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.entity.CourseMedia;
import com.gudgo.jeju.domain.planner.courseMedia.entity.QCourseMedia;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseMediaQueryService {
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public CourseMediaQueryService(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CourseMediaResponseDto> getMedias(Long courseId) {
        QCourseMedia qCourseMedia = QCourseMedia.courseMedia;

        List<CourseMedia> courseMedias = jpaQueryFactory
                .selectFrom(qCourseMedia)
                .where(qCourseMedia.planner.id.eq(courseId)
                        .and(qCourseMedia.isDeleted.isFalse()))
                .fetch();

        List<CourseMediaResponseDto> courseMediaResponseDtos = courseMedias.stream()
                .map(courseMedia ->
                        new CourseMediaResponseDto(
                                courseMedia.getId(),
                                courseMedia.getPlanner().getId(),
                                courseMedia.getImageUrl(),
                                courseMedia.getContent(),
                                courseMedia.getLatitude(),
                                courseMedia.getLongitude()
                        ))
                .toList();

        return courseMediaResponseDtos;
    }
}
