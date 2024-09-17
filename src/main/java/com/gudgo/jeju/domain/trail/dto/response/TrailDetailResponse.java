package com.gudgo.jeju.domain.trail.dto.response;

import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;

import java.util.List;

public record TrailDetailResponse(
        String title,
        double avgStar,
        List<String> images,
        String summary,
        String address,
        String time,
        String content,
        String tel,
        double latitude,
        double longitude,
        List<TrailRecommendResponse> recommends,
        Long reviewCount,
        List<ReviewResponse> reviews
) {
}
