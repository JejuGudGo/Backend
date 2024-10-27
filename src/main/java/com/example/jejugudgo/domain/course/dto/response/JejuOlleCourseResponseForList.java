package com.example.jejugudgo.domain.course.dto.response;

public record JejuOlleCourseResponseForList(
        Long jejuOlleCourseId,
        String title,
        String startSpot,
        String endSpot,
        String TotalTime,
        double starAvg,
        Long reviewCount
) {
}
