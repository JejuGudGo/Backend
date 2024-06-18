package com.gudgo.jeju.domain.post.dto.response;

public record CoursePostResponse(
        Long id,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long numberTag,
        Long courseId,
        String title,
        Long participantNum,
        Long currentParticipantNum,
        String content
) {
}
