package com.example.jejugudgo.domain.user.course.jejuGudgo.dto.reseponse;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserJejugudgoCourseResponse(
        LocalDate finishedAt,
        String courseTitle,
        LocalTime time,
        double distance,
        Long steps,
        String imageUrl,
        LocalTime restTime,
        double averageSpeed,
        double averagePace,
        double kcal
) {
}
