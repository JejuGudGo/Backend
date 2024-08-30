package com.gudgo.jeju.domain.planner.review.dto.request;


import java.util.List;

public record ReviewCategoryRequestDto(
        String code,
//        String title,
        List<ReviewTagRequestDto> tags

) {
}
