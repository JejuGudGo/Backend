package com.gudgo.jeju.domain.trail.dto.response;

import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;
import com.gudgo.jeju.domain.review.dto.response.TopRatingTagResponseDto;

import java.util.List;

public record TrailDetailResponse(
        String title,
        double avgStar,
        String summary,
        List<String> images,
        String address,
        String content,
        String tel,
        double latitude,
        double longitude,
        String informationUrl,
        String useTime,
        String fee,
        List<TopRatingTagResponseDto> topRatingTags,
        List<TrailRecommendResponse> recommends,
        Long reviewCount,
        List<ReviewResponse> reviews
) {
}
