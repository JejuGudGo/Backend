package com.gudgo.jeju.global.data.review.service;


import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewImage;
import com.gudgo.jeju.global.data.review.repository.ReviewImageRepository;
import com.gudgo.jeju.global.data.review.repository.ReviewRepository;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ImageUpdateService imageUpdateService;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public void uploadImages (Long userId, Long plannerReviewId, MultipartFile[] images) throws Exception {

        Review review = reviewRepository.findById(plannerReviewId)
                .orElseThrow(EntityNotFoundException::new);


        for (MultipartFile image : images) {
            Path path = imageUpdateService.saveImage(userId, image, Category.REVIEW);

            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .imageUrl(path.toString())
                    .isDeleted(false)
                    .build();

            reviewImageRepository.save(reviewImage);
        }
    }
}
