package com.example.jejugudgo.domain.review.dto.response;

import java.util.List;

public record ReviewResponse (
        Long reviewId,
        String profileImgUrl,
        String nickname,
        double star,
        String createdAt,
        List<String> keywords,
        List<String> reviewImgUrls,
        String content
) {
}
