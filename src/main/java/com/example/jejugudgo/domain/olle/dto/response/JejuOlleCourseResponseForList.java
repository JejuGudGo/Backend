package com.example.jejugudgo.domain.olle.dto.response;

public record JejuOlleCourseResponseForList(
        Long jejuOlleCourseId,
        String title,
        String startSpot,
        String endSpot,
        String totalTime,
        double starAvg,
        Long reviewCount
) {
}
