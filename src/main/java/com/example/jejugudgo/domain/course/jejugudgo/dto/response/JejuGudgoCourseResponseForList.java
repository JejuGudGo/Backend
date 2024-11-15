package com.example.jejugudgo.domain.course.jejugudgo.dto.response;

import java.time.LocalTime;

public record JejuGudgoCourseResponseForList(
        Long jejuGudgoCourseId,
        String title,
        String startSpot,
        String endSpot,
        LocalTime TotalTime,
        double starAvg,
        Long reviewCount
) {
}
