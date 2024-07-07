package com.gudgo.jeju.domain.review.service;


import com.gudgo.jeju.domain.review.entity.PlannerReview;
import com.gudgo.jeju.domain.review.entity.PlannerReviewImage;
import com.gudgo.jeju.domain.review.repository.PlannerReviewImageRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewRepository;
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
    private final PlannerReviewRepository plannerReviewRepository;
    private final PlannerReviewImageRepository plannerReviewImageRepository;

    @Transactional
    public void uploadImages (Long userId, Long plannerReviewId, MultipartFile[] images) throws Exception {

        PlannerReview plannerReview = plannerReviewRepository.findById(plannerReviewId)
                .orElseThrow(EntityNotFoundException::new);


        for (MultipartFile image : images) {
            Path path = imageUpdateService.saveImage(userId, image, Category.REVIEW);

            PlannerReviewImage reviewImage = PlannerReviewImage.builder()
                    .plannerReview(plannerReview)
                    .imageUrl(path.toString())
                    .build();

            plannerReviewImageRepository.save(reviewImage);
        }
    }
}
