package com.gudgo.jeju.domain.planner.query;

import com.gudgo.jeju.domain.course.entity.QParticipant;
import com.gudgo.jeju.domain.planner.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.planner.entity.Participant;
import com.gudgo.jeju.domain.planner.entity.QCourse;
import com.gudgo.jeju.domain.planner.entity.QParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ParticipantResponse> getApprovedParticipants(Long courseId) {
        QParticipant qParticipant = QParticipant.participant;

        List<Participant> participants = queryFactory
                .selectFrom(qParticipant)
                .where(qParticipant.course.id.eq(courseId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isTrue())
                )
                .fetch();

        List<ParticipantResponse> participantResponses = participants.stream()
                .map(participant ->
                        new ParticipantResponse(
                                participant.getId(),
                                participant.getCourse().getId(),
                                participant.getParticipantUserId()
                        )).toList();

        return participantResponses;
    }

    public List<ParticipantResponse> getUnApprovedParticipants(Long courseId) {
        QParticipant qParticipant = QParticipant.participant;

        List<Participant> participants = queryFactory
                .selectFrom(qParticipant)
                .where(qParticipant.course.id.eq(courseId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isFalse())
                )
                .fetch();

        List<ParticipantResponse> participantResponses = participants.stream()
                .map(participant ->
                        new ParticipantResponse(
                                participant.getId(),
                                participant.getCourse().getId(),
                                participant.getParticipantUserId()
                        )).toList();

        return participantResponses;
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

    public Participant findParticipantIdByChatRoomIdAndUserId(Long chatRoomId, Long userId) {
        QPlanner qPlanner = QPlanner.planner;
        QParticipant qParticipant = QParticipant.participant;

        Participant participant = queryFactory
                .selectFrom(qParticipant)
                .join(qParticipant.planner, qPlanner)
                .where(qPlanner.chatRoom.id.eq(chatRoomId)
                        .and(qParticipant.participantUserId.eq(userId)))
                .fetchOne();

        return participant;
    }
}
