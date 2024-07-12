package com.gudgo.jeju.global.data.review.dto;

import java.util.List;

public record ReviewCategoryResponse(
        String code,
        List<ReviewTagResponse> tags
) {
}
