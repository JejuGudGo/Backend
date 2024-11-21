package com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request;

import java.time.LocalTime;

public record UserJejuGudgoCourseCompletedRequest(
        LocalTime time,
        LocalTime restTime,
        double distance,
        double speed,
        double kcal,
        Long steps
) {
}
