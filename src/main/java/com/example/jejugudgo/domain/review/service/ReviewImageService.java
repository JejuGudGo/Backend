package com.example.jejugudgo.domain.review.service;

import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewImage;
import com.example.jejugudgo.domain.review.repository.ReviewImageRepository;
import com.example.jejugudgo.global.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewImageRepository reviewImageRepository;
    private final ImageUtil imageUtil;

    /* create */
    public void createReviewImages(List<MultipartFile> images, Long userId, Review review) throws Exception {
        if (!images.isEmpty()) {
            for (MultipartFile image : images) {
                Path path = imageUtil.saveImage(userId, image, "reviews");
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(path.toString())
                        .review(review)
                        .build();

                reviewImageRepository.save(reviewImage);
            }
        }
    }
}
