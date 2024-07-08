package com.gudgo.jeju.domain.review.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ReviewRequestDto(
        String content,
        LocalDate createdAt,
        List<ReviewCategoryRequestDto> categoriesAndTags
) {

}
