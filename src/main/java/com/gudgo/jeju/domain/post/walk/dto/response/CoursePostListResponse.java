package com.gudgo.jeju.domain.post.walk.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CoursePostListResponse(
        Long postId,
        String status,
        String title,
        String courseImgUrl,
        String courseSummary,
        LocalDate startDate,
        LocalTime startAt,
        LocalDateTime createAt,
        Long currentParticipantNum,
        Long maxParticipantNum
) {
}
