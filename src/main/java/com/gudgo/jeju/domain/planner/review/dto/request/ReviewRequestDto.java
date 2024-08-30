package com.gudgo.jeju.domain.planner.review.dto.request;

import java.time.LocalDate;
import java.util.List;

public record ReviewRequestDto(
        String content,
        LocalDate createdAt,
        List<ReviewCategoryRequestDto> categories,
        Long stars
) {

}
