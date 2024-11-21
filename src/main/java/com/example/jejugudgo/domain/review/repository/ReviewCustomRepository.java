package com.example.jejugudgo.domain.review.repository;

import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;
import com.example.jejugudgo.domain.review.enums.ReviewType;

import java.util.List;

public interface ReviewCustomRepository {
    List<TopFiveRankedKeywordResponse> getTopCategoriesForCourse(ReviewType type, Long courseId);
}
