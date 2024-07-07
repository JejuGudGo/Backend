package com.gudgo.jeju.domain.review.dto.request;

import java.util.List;
import java.util.Map;

public record ReviewRequestDto(
        String content,
        List<ReviewCategoryRequestDto> categoriesAndTags
) {

}
