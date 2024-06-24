package com.gudgo.jeju.domain.course.dto.response;

public record ParticipantResponse(
        Long id,
        Long courseId,
        Long participantUserId
) {
}