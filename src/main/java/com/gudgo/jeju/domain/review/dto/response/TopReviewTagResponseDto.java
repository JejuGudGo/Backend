package com.gudgo.jeju.domain.review.dto.response;

import com.gudgo.jeju.domain.review.entity.ReviewFilterTag;

public record TopReviewTagResponseDto(
        ReviewFilterTag tag,
        Long count
) {
}
