package com.gudgo.jeju.domain.post.walk.dto.response;

public record CoursePostSpotResponse(
        String title,
        Long order,
        double latitude,
        double longitude
) {
}
