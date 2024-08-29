package com.gudgo.jeju.domain.post.walk.dto.response;

public record CoursePostResponse(
        Long id,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long numberTag,
        String title,
        Long participantNum,
        Long currentParticipantNum,
        String content
) {
}
