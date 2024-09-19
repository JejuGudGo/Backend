package com.gudgo.jeju.domain.review.dto.request;


import com.gudgo.jeju.domain.review.entity.PurposeType;
import com.gudgo.jeju.domain.review.entity.ReviewCategory;
import com.gudgo.jeju.domain.review.entity.ReviewFilterTag;
import com.gudgo.jeju.domain.review.entity.TogetherType;

import java.util.List;

public record UserCourseReviewRequest(
        Long stars,
        ReviewCategory reviewCategory,
        List<PurposeType> purposeTypes,
        List<TogetherType> togetherTypes,
        List<ReviewFilterTag> reviewTags, // 소분류
        String content
) {
}
