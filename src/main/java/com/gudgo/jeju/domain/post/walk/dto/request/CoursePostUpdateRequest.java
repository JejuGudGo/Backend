package com.gudgo.jeju.domain.post.walk.dto.request;

public record CoursePostUpdateRequest(
        String title,
        String content,
        Long plannerId,
        Long participantNum,
        boolean isFinished
) {
}
