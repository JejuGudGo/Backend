package com.gudgo.jeju.domain.post.participant.dto.response;

public record ParticipantResponse(
        Long id,
        Long courseId,
        Long participantUserId
) {
}