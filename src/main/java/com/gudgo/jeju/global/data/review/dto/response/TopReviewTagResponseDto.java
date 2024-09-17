package com.gudgo.jeju.global.data.review.dto.response;

import com.gudgo.jeju.global.data.review.entity.ReviewFilterTag;

public record TopReviewTagResponseDto(
        ReviewFilterTag tag,
        Long count
) {
}
