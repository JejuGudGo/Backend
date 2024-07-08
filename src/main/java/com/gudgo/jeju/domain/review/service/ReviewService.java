package com.gudgo.jeju.domain.review.service;

import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.review.dto.request.ReviewCategoryRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewTagRequestDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewCategoryResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewPostResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewTagResponseDto;
import com.gudgo.jeju.domain.review.entity.PlannerReview;
import com.gudgo.jeju.domain.review.entity.PlannerReviewCategory;
import com.gudgo.jeju.domain.review.entity.PlannerReviewImage;
import com.gudgo.jeju.domain.review.entity.PlannerReviewTag;
import com.gudgo.jeju.domain.review.query.ReviewImageQueryService;
import com.gudgo.jeju.domain.review.repository.PlannerReviewCategoryRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewImageRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewTagRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.data.review.dto.ReviewDataResponseDto;
import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.repository.ReviewRepository;
import com.gudgo.jeju.global.data.review.repository.ReviewTagRepository;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final PlannerReviewRepository plannerReviewRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerReviewCategoryRepository plannerReviewCategoryRepository;
    private final PlannerReviewTagRepository plannerReviewTagRepository;
    private final UserRepository userRepository;

    private final ReviewImageService reviewImageService;
    private final ReviewImageQueryService reviewImageQueryService;
    @Transactional
    public ReviewPostResponseDto create(Long plannerId, Long userId, ReviewRequestDto requestDto, MultipartFile[] images) throws Exception {


        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        PlannerReview plannerReview = PlannerReview.builder()
                .planner(planner)
                .user(user)
                .content(requestDto.content())
                .createdAt(LocalDate.now())
                .isDeleted(false)
                .build();

        plannerReviewRepository.save(plannerReview);

        reviewImageService.uploadImages(userId, plannerReview.getId(), images);

        List<ReviewCategoryResponseDto> categoryResponses = new ArrayList<>();
        for (ReviewCategoryRequestDto categoryRequest : requestDto.categoriesAndTags()) {
            PlannerReviewCategory plannerReviewCategory = PlannerReviewCategory.builder()
                    .plannerReview(plannerReview)
                    .code(categoryRequest.code())
//                    .title(categoryRequest.title())
                    .build();
            plannerReviewCategoryRepository.save(plannerReviewCategory);

            List<ReviewTagResponseDto> tagResponses = categoryRequest.tags().stream()
                    .map(tagResponse -> {
                        PlannerReviewTag plannerReviewTag = PlannerReviewTag.builder()
                                .plannerReviewCategory(plannerReviewCategory)
                                .code(tagResponse.code())
//                                .title(tagResponse.title())
                                .isDeleted(false)
                                .build();
                        plannerReviewTagRepository.save(plannerReviewTag);

                        return new ReviewTagResponseDto(
                                plannerReviewTag.getId(),
                                plannerReviewCategory.getId(),
                                tagResponse.code()
//                                tagResponse.title()
                        );
                    })
                    .collect(Collectors.toList());

            categoryResponses.add(new ReviewCategoryResponseDto(
                    plannerReviewCategory.getId(),
                    plannerReview.getId(),
                    plannerReviewCategory.getCode(),
//                    plannerReviewCategory.getTitle(),
                    tagResponses
            ));
        }

        ReviewPostResponseDto responseDto = new ReviewPostResponseDto(
                plannerReview.getId(),
                plannerReview.getPlanner().getId(),
                plannerReview.getContent(),
                plannerReview.getCreatedAt(),
                reviewImageQueryService.getReviewImages(plannerReview.getId()),
                categoryResponses
        );

        return responseDto;

    }


    @Transactional
    public void delete(Long reviewId) {
        PlannerReview plannerReview = plannerReviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        plannerReview = plannerReview.withIsDeleted(true);

        plannerReviewRepository.save(plannerReview);
    }
}
