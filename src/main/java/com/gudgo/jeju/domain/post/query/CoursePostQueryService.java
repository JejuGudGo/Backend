package com.gudgo.jeju.domain.post.query;

import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.QCourse;
import com.gudgo.jeju.domain.course.entity.QParticipant;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePostQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CoursePostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
        QCourse qCourse = QCourse.course;

        List<Course> courses = queryFactory
                .selectFrom(qCourse)
                .where(qCourse.post.isDeleted.isFalse()
                        .and(qCourse.post.isFinished.isFalse()))
                .fetch();

        List<CoursePostResponse> coursePostResponses = courses.stream()
                .map(course ->
                        new CoursePostResponse(
                                course.getPost().getId(),
                                course.getPost().getUser().getId(),
                                course.getPost().getUser().getNickname(),
                                course.getPost().getUser().getProfile().getProfileImageUrl(),
                                course.getPost().getUser().getNumberTag(),
                                course.getPost().getTitle(),
                                course.getPost().getCompanionsNum(),
                                getCurrentParticipantNum(course.getId()),
                                course.getPost().getContent()
                        ))
                .toList();
        return PaginationUtil.listToPage(coursePostResponses, pageable);
    }

    private Long getCurrentParticipantNum(Long courseId) {
        QParticipant qParticipant = QParticipant.participant;

        Long currentParticipantNum = queryFactory
                .select(qParticipant.count())
                .from(qParticipant)
                .where((qParticipant.course.id.eq(courseId)
                        .and(qParticipant.approved.isTrue())
                        .and(qParticipant.isDeleted.isFalse())))
                .fetchOne();

        return currentParticipantNum;
    }
}
