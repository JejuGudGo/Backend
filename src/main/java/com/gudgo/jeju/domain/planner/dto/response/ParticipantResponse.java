package com.gudgo.jeju.domain.planner.dto.response;

public record ParticipantResponse(
        Long id,
        Long courseId,
        Long participantUserId
) {
}