package com.gudgo.jeju.domain.review.dto.response;

import com.gudgo.jeju.domain.review.entity.PlannerReviewTag;

import java.util.List;

public record PlannerReviewCategoryResponse(
        String code,
        List<PlannerReviewTag> tags
) {
}
