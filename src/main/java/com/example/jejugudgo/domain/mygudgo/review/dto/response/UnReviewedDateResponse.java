package com.example.jejugudgo.domain.mygudgo.review.dto.response;

import java.time.LocalDate;

public record UnReviewedDateResponse(
        String courseType,
        Long targetId,
        LocalDate finishedAt
) {
}
