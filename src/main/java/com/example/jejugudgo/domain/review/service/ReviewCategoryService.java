package com.example.jejugudgo.domain.review.service;

import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import com.example.jejugudgo.domain.review.enums.ReviewCategory1;
import com.example.jejugudgo.domain.review.enums.ReviewCategory2;
import com.example.jejugudgo.domain.review.enums.ReviewCategory3;
import com.example.jejugudgo.domain.review.repository.ReviewCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCategoryService {
    private final ReviewCategoryRepository reviewCategoryRepository;

    /* create */
    public List<ReviewCategory> createReviewCategory(List<String> category1, List<String> category2, List<String> category3, Review review) {
        List<ReviewCategory> reviewCategories = new ArrayList<>();

        if (!category1.isEmpty()) {
            for (String category : category1) {
                ReviewCategory reviewCategory = ReviewCategory.builder()
                        .category1(ReviewCategory1.fromQuery(category))
                        .review(review)
                        .build();

                reviewCategoryRepository.save(reviewCategory);
                reviewCategories.add(reviewCategory);
            }
        }

        if (!category2.isEmpty()) {
            for (String category : category2) {
                ReviewCategory reviewCategory = ReviewCategory.builder()
                        .category2(ReviewCategory2.fromQuery(category))
                        .review(review)
                        .build();

                reviewCategoryRepository.save(reviewCategory);
                reviewCategories.add(reviewCategory);
            }
        }

        if (!category3.isEmpty()) {
            for (String category : category3) {
                ReviewCategory reviewCategory = ReviewCategory.builder()
                        .category3(ReviewCategory3.fromQuery(category))
                        .review(review)
                        .build();

                reviewCategoryRepository.save(reviewCategory);
                reviewCategories.add(reviewCategory);
            }
        }

        return reviewCategories;
    }
}
