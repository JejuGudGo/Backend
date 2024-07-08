package com.gudgo.jeju.domain.review.dto.request;

import java.util.List;

public record ReviewUpdateRequestDto(
        String content,
        List<ReviewCategoryRequestDto> categories
) {
}
