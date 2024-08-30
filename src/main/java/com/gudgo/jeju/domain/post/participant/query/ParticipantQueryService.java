package com.gudgo.jeju.domain.post.participant.query;

import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.post.participant.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.post.participant.entity.QParticipant;
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

    public Long countCourseParticipant(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QParticipant qParticipant = QParticipant.participant;

        Long count = queryFactory
                .select(qParticipant.countDistinct())
                .from(qParticipant)
                .join(qParticipant.planner, qPlanner)
                .where(qPlanner.id.eq(plannerId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isTrue())
                )
                .fetchOne();

        return count;
    }

    public List<ParticipantResponse> getApprovedParticipants(Long plannerId) {
        QParticipant qParticipant = QParticipant.participant;

        List<Participant> participants = queryFactory
                .selectFrom(qParticipant)
                .where(qParticipant.planner.id.eq(plannerId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isTrue())
                )
                .fetch();

        List<ParticipantResponse> participantResponses = participants.stream()
                .map(participant ->
                        new ParticipantResponse(
                                participant.getId(),
                                participant.getPlanner().getId(),
                                participant.getUser().getId()
                        )).toList();

        return participantResponses;
    }

    public List<ParticipantResponse> getUnApprovedParticipants(Long plannerId) {
        QParticipant qParticipant = QParticipant.participant;

        List<Participant> participants = queryFactory
                .selectFrom(qParticipant)
                .where(qParticipant.planner.id.eq(plannerId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qParticipant.approved.isFalse())
                )
                .fetch();

        List<ParticipantResponse> participantResponses = participants.stream()
                .map(participant ->
                        new ParticipantResponse(
                                participant.getId(),
                                participant.getPlanner().getId(),
                                participant.getUser().getId()
                        )).toList();

        return participantResponses;
    }

    public Participant findParticipantIdByChatRoomIdAndUserId(Long chatRoomId, Long userId) {
        QPlanner qPlanner = QPlanner.planner;
        QParticipant qParticipant = QParticipant.participant;

        Participant participant = queryFactory
                .selectFrom(qParticipant)
                .join(qParticipant.planner, qPlanner)
                .where(qPlanner.chatRoom.id.eq(chatRoomId)
                        .and(qParticipant.user.id.eq(userId)))
                .fetchOne();

        return participant;
    }
}
