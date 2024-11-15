package com.example.jejugudgo.domain.review.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StarAvgCalculator {

    public double validateCourseByType(double currentAvg, int currentSize, Long stars) {
        double newStartAvg = 0.0;

        if (currentSize == 0 || currentAvg == 0.0) {
            return (double) stars;
        }

        double sumStars = (currentAvg * currentSize) / 100;
        newStartAvg = (sumStars + stars) / (currentSize + 1);
        return newStartAvg;
    }
}
