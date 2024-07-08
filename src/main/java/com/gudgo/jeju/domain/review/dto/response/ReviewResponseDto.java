package com.gudgo.jeju.domain.review.dto.response;

import com.gudgo.jeju.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponseDto(
        Long id,
        Long plannerId,
        Long userId,
        String content,
        LocalDate createdAt,
        List<ReviewImageResponseDto> reviewImages,
        List<ReviewCategoryResponseDto> reviewCategories


) {
}
