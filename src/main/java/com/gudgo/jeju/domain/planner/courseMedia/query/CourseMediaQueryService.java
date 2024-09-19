package com.gudgo.jeju.domain.planner.courseMedia.query;

import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaBackImagesResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.entity.CourseMedia;
import com.gudgo.jeju.domain.planner.courseMedia.entity.QCourseMedia;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseMediaQueryService {
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public CourseMediaQueryService(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CourseMediaResponseDto> getAllMedias(Long userId) {
        QPlanner qPlanner = QPlanner.planner;
        QCourseMedia qCourseMedia = QCourseMedia.courseMedia;

        List<CourseMedia> courseMedias = jpaQueryFactory
                .selectFrom(qCourseMedia)
                .join(qCourseMedia.planner, qPlanner)
                .where(qPlanner.user.id.eq(userId)
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue())
                        .and(qCourseMedia.isDeleted.eq(false)))
                .fetch();

        return courseMedias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseMediaBackImagesResponseDto> getAllBackImages(Long userId) {
        QPlanner qPlanner = QPlanner.planner;
        QCourseMedia qCourseMedia = QCourseMedia.courseMedia;

        List<CourseMedia> courseMedias = jpaQueryFactory
                .selectFrom(qCourseMedia)
                .join(qCourseMedia.planner, qPlanner)
                .where(qPlanner.user.id.eq(userId)
                        .and(qPlanner.isDeleted.isFalse())
                        .and(qPlanner.isCompleted.isTrue())
                        .and(qCourseMedia.isDeleted.eq(false)))
                .fetch();

        List<CourseMediaBackImagesResponseDto> CourseMediaBackImagesResponseDto = courseMedias.stream()
                .map(courseMedia ->
                        new CourseMediaBackImagesResponseDto(
                                courseMedia.getId(),
                                courseMedia.getBackImageUrl(),
                                courseMedia.getLatitude(),
                                courseMedia.getLongitude()
                        ))
                .toList();

        return CourseMediaBackImagesResponseDto;
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
                                courseMedia.getSelfieImageUrl(),
                                courseMedia.getBackImageUrl(),
                                courseMedia.getContent(),
                                courseMedia.getLatitude(),
                                courseMedia.getLongitude()
                        ))
                .toList();

        return courseMediaResponseDtos;
    }
    private CourseMediaResponseDto convertToDto(CourseMedia courseMedia) {
        return new CourseMediaResponseDto(
                courseMedia.getId(),
                courseMedia.getContent(),
                courseMedia.getSelfieImageUrl(),
                courseMedia.getBackImageUrl(),
                courseMedia.getLatitude(),
                courseMedia.getLongitude()

        );

    }
}
