package com.example.jejugudgo.domain.mygudgo.review.dto.response;


import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record Review(
        Long id,
        String reviewerProfileImageUrl,
        String reviewerNickname,
        double star, /* [주의] 별점 평균 아님 */
        LocalDateTime visitedAt,
        List<String> tags,
        @Nullable List<String> imageUrls,
        String content
) {
}
