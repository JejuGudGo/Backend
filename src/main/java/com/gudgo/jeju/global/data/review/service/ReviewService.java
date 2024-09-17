package com.gudgo.jeju.global.data.review.service;

import com.gudgo.jeju.domain.planner.course.validation.PlannerValidator;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.trail.repository.TrailRepository;
import com.gudgo.jeju.global.data.review.dto.request.UserCourseReviewRequest;
import com.gudgo.jeju.global.data.review.entity.*;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.data.review.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewPurposeTagRepository reviewPurposeTagRepository;
    private final ReviewTogetherTagRepository reviewTogetherTagRepository;

    private final ReviewImageService reviewImageService;
    private final PlannerValidator plannerValidator;
    private final ReviewTagRepository reviewTagRepository;
    private final TrailRepository trailRepository;

    @Transactional
    public void createTrailReview(Long trailId, Long userId, UserCourseReviewRequest requestDto, MultipartFile[] images) throws Exception {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Review review = Review.builder()
                .trail(trail)
                .user(user)
                .reviewCategory(ReviewCategory.TRAIL)
                .content(requestDto.content())
                .createdAt(LocalDate.now())
                .isDeleted(false)
                .stars(requestDto.stars())
                .build();

        reviewRepository.save(review);

        if (images != null && images.length > 0) {
            reviewImageService.uploadImages(userId, review.getId(), images);
        }

        saveTags(requestDto, review);
    }

    @Transactional
    public void createUserCourseReview(Long plannerId, Long userId, UserCourseReviewRequest requestDto, MultipartFile[] images) throws Exception {
        Planner planner = plannerValidator.validatePlanner(plannerId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Review review = Review.builder()
                .planner(planner)
                .user(user)
                .reviewCategory(ReviewCategory.USER)
                .content(requestDto.content())
                .createdAt(LocalDate.now())
                .isDeleted(false)
                .stars(requestDto.stars())
                .build();

        reviewRepository.save(review);

        if (images != null && images.length > 0) {
            reviewImageService.uploadImages(userId, review.getId(), images);
        }

        saveTags(requestDto, review);
    }

    @Transactional
    public void updateCourseReview(Long reviewId, UserCourseReviewRequest requestDto, MultipartFile[] images) throws Exception {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        LocalDate currentDate = LocalDate.now();
        LocalDate createdAt = review.getCreatedAt();
        long daysBetween = ChronoUnit.DAYS.between(createdAt, currentDate);

        if (daysBetween > 7) {
            throw new RuntimeException("수정할 수 있는 기간이 지났습니다. 7일 이내에만 수정이 가능합니다.");
        }

        // 내용, 별점 수정
        review = review.withContent(requestDto.content());
        review = review.withStars(requestDto.stars());
        reviewRepository.save(review);

        // 이미지 수정
        if (images != null && images.length > 0) {
            updateReviewImages(review, images);
        }

        // 태그 수정
        updateReviewTags(review, requestDto);
    }

    @Transactional
    public void deleteCourseReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        review = review.withIsDeleted(true);
        reviewRepository.save(review);
    }

    private void saveTags(UserCourseReviewRequest requestDto, Review review) {
        List<PurposeType> purposeTypes = requestDto.purposeTypes();
        for (PurposeType purposeType : purposeTypes) {
            ReviewPurposeTag purposeTag = ReviewPurposeTag.builder()
                    .review(review)
                    .purposeType(purposeType)
                    .isDeleted(false)
                    .build();

            reviewPurposeTagRepository.save(purposeTag);
        }

        List<TogetherType> togetherTypes = requestDto.togetherTypes();
        for (TogetherType togetherType : togetherTypes) {
            ReviewTogetherTag reviewTogetherTag = ReviewTogetherTag.builder()
                    .review(review)
                    .togetherType(togetherType)
                    .isDeleted(false)
                    .build();

            reviewTogetherTagRepository.save(reviewTogetherTag);
        }

        List<ReviewFilterTag> reviewFilterTags = requestDto.reviewTags();
        for (ReviewFilterTag filterTag : reviewFilterTags) {
            ReviewTag reviewTag  = ReviewTag.builder()
                    .review(review)
                    .filterTag(filterTag)
                    .isDeleted(false)
                    .build();

            reviewTagRepository.save(reviewTag);
        }
    }

    private void updateReviewImages(Review review, MultipartFile[] images) throws Exception {
        // 기존 이미지들 isdeleted=true 처리
        List<ReviewImage> preImages = reviewImageRepository.findByReview(review);

        for (ReviewImage preImage : preImages) {
            preImage = preImage.withIsDeleted(true);
            reviewImageRepository.save(preImage);
        }

        // 새로운 이미지 저장
        reviewImageService.uploadImages(review.getUser().getId(), review.getId(), images);
    }

    private void updateReviewTags(Review review, UserCourseReviewRequest requestDto) {
        // 기존 태그들 isdeleted=true 처리
        List<ReviewPurposeTag> purposeTags = reviewPurposeTagRepository.findByReview(review);
        List<ReviewTogetherTag> togetherTags = reviewTogetherTagRepository.findByReview(review);
        List<ReviewTag> reviewTags = reviewTagRepository.findByReview(review);

        for (ReviewPurposeTag purposeTag : purposeTags) {
            purposeTag = purposeTag.withIsDeleted();
            reviewPurposeTagRepository.save(purposeTag);
        }

        for (ReviewTogetherTag togetherTag : togetherTags) {
            togetherTag = togetherTag.withIsDeleted();
            reviewTogetherTagRepository.save(togetherTag);
        }

        for (ReviewTag reviewTag : reviewTags) {
            reviewTag = reviewTag.withIsDeleted();
            reviewTagRepository.save(reviewTag);
        }

        // 새로운 카테고리와 태그 저장
        saveTags(requestDto, review);
    }
}
