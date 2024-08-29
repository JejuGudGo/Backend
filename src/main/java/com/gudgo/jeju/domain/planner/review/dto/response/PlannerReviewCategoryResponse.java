package com.gudgo.jeju.domain.planner.review.dto.response;

import com.gudgo.jeju.domain.planner.review.entity.PlannerReviewTag;

import java.util.List;

public record PlannerReviewCategoryResponse(
        String code,
        List<PlannerReviewTag> tags
) {
}
