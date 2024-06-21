package com.gudgo.jeju.domain.course.query;

import com.gudgo.jeju.domain.course.entity.Participant;
import com.gudgo.jeju.domain.course.entity.QCourse;
import com.gudgo.jeju.domain.course.entity.QParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ParticipantQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long countCourseParticipant(Long courseId) {
        QCourse qCourse = QCourse.course;
        QParticipant qParticipant = QParticipant.participant;

        Long count = queryFactory
                .select(qParticipant.countDistinct())
                .from(qParticipant)
                .join(qParticipant.course, qCourse)
                .where(qCourse.id.eq(courseId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isTrue())
                )
                .fetchOne();

        return count;
    }

    public Participant findParticipantByUserId(Long courseId, Long userId) {
        QCourse qCourse = QCourse.course;
        QParticipant qParticipant = QParticipant.participant;

        Participant participant = queryFactory
                .selectFrom(qParticipant)
                .join(qParticipant.course, qCourse)
                .where(qCourse.id.eq(courseId)
                        .and(qParticipant.participantUserId.eq(userId)))
                .fetchOne();

        return participant;
    }
}
