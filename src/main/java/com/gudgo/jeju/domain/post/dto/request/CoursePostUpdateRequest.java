package com.gudgo.jeju.domain.post.dto.request;

public record CoursePostUpdateRequest(
        String title,
        String content,
        Long plannerId,
        Long participantNum,
        boolean isFinished
) {
}
