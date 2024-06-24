package com.gudgo.jeju.domain.course.query;

import com.gudgo.jeju.domain.course.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.course.entity.CourseMedia;
import com.gudgo.jeju.domain.course.entity.QCourseMedia;
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
                .where(qCourseMedia.course.id.eq(courseId)
                        .and(qCourseMedia.isDeleted.isFalse()))
                .fetch();

        List<CourseMediaResponseDto> courseMediaResponseDtos = courseMedias.stream()
                .map(courseMedia ->
                        new CourseMediaResponseDto(
                                courseMedia.getId(),
                                courseMedia.getCourse().getId(),
                                courseMedia.getImageUrl(),
                                courseMedia.getContent(),
                                courseMedia.getLatitude(),
                                courseMedia.getLongitude()
                        ))
                .toList();

        return courseMediaResponseDtos;
    }
}
