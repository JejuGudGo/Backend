package com.gudgo.jeju.global.data.review.dto.request;

import com.gudgo.jeju.global.data.review.entity.PurposeType;
import com.gudgo.jeju.global.data.review.entity.ReviewCategory;
import com.gudgo.jeju.global.data.review.entity.ReviewFilterTag;
import com.gudgo.jeju.global.data.review.entity.TogetherType;

import java.time.LocalDate;
import java.util.List;

public record TrailReviewRequest(
        Long stars,
        LocalDate finishAt,
        ReviewCategory reviewCategory,
        List<PurposeType> purposeTypes,
        List<TogetherType> togetherTypes,
        List<ReviewFilterTag> reviewTags, // 소분류
        String content
) {
}
