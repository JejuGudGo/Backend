package com.gudgo.jeju.domain.review.dto.request;


import java.util.List;

public record ReviewCategoryRequestDto(
        String code,
//        String title,
        List<ReviewTagRequestDto> tags

) {
}
