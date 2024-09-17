package com.gudgo.jeju.domain.review.dto.response;

import java.util.List;

public record PlannerReviewResponse (
    List<TopRatingTagResponseDto> topRatingTags,
    List<ReviewResponse> reviews
) {}