package com.example.jejugudgo.domain.course.dto.response;

public record JejuGudgoCourseResponseForList(
        Long jejuGudgoCourseId,
        String title,
        String startSpot,
        String endSpot,
        Long TotalTime,
        double starAvg,
        Long reviewCount
) {
}
