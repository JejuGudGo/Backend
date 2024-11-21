package com.example.jejugudgo.domain.review.dto.request;

public record StarAvgUpdateRequest(
        Long courseId,
        double newStarAvg
) {
}
