package com.gudgo.jeju.global.data.review.service;


import com.gudgo.jeju.global.data.review.dto.ReviewCategoryResponse;
import com.gudgo.jeju.global.data.review.dto.ReviewTagResponse;
import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewTag;
import com.gudgo.jeju.global.data.review.repository.ReviewRepository;
import com.gudgo.jeju.global.data.review.repository.ReviewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewTagRepository reviewTagRepository;
    private final ReviewRepository reviewRepository;

    // 태그가 얼마 안되서, 이곳에서 순회하겠습니다.
    public List<ReviewCategoryResponse> getCategoryAndTags() {
        List<ReviewCategoryResponse> responses = new ArrayList<>();
        List<Review> categories = reviewRepository.findAll();

        for (Review category : categories) {
            List<ReviewTagResponse> tagResponses = new ArrayList<>();
            List<ReviewTag> tags = reviewTagRepository.findByReviewId(category.getId());

            for (ReviewTag tag : tags) {
                ReviewTagResponse response = new ReviewTagResponse(tag.getCode());
                tagResponses.add(response);
            }

            ReviewCategoryResponse response = new ReviewCategoryResponse(
                    category.getCode(),
                    tagResponses
            );

            responses.add(response);
        }

        return responses;
    }
}
