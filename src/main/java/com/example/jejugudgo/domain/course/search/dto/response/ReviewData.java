package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.mygudgo.review.dto.response.KeywordReview;
import com.example.jejugudgo.domain.mygudgo.review.dto.response.Review;

import java.util.List;

public record ReviewData(
        List<KeywordReview> keywordReviews,
        Long reviewCount,
        List<Review> reviews
) {
}
