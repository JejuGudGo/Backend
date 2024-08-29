package com.gudgo.jeju.domain.review.service;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.review.dto.request.ReviewCategoryRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewTagRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewUpdateRequestDto;
import com.gudgo.jeju.domain.review.dto.response.*;
import com.gudgo.jeju.domain.review.entity.PlannerReview;
import com.gudgo.jeju.domain.review.entity.PlannerReviewCategory;
import com.gudgo.jeju.domain.review.entity.PlannerReviewImage;
import com.gudgo.jeju.domain.review.entity.PlannerReviewTag;
import com.gudgo.jeju.domain.review.query.ReviewCategoryQueryService;
import com.gudgo.jeju.domain.review.query.ReviewImageQueryService;
import com.gudgo.jeju.domain.review.repository.PlannerReviewCategoryRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewImageRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewRepository;
import com.gudgo.jeju.domain.review.repository.PlannerReviewTagRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserReviewService {
    private final PlannerReviewRepository plannerReviewRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerReviewCategoryRepository plannerReviewCategoryRepository;
    private final PlannerReviewTagRepository plannerReviewTagRepository;
    private final UserRepository userRepository;
    private final PlannerReviewImageRepository plannerReviewImageRepository;

    private final ReviewImageService reviewImageService;
    private final ReviewImageQueryService reviewImageQueryService;
    private final ReviewCategoryQueryService reviewCategoryQueryService;

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
                .stars(requestDto.stars())
                .build();

        plannerReviewRepository.save(plannerReview);

        reviewImageService.uploadImages(userId, plannerReview.getId(), images);

        // 카테고리 태그 저장, responseDto 생성
        List<ReviewCategoryResponseDto> categoryResponses = saveCategoriesAndTags(plannerReview, requestDto.categories());

        return new ReviewPostResponseDto(
                plannerReview.getId(),
                plannerReview.getPlanner().getId(),
                plannerReview.getContent(),
                plannerReview.getStars(),
                plannerReview.getCreatedAt(),
                reviewImageQueryService.getReviewImages(plannerReview.getId()),
                categoryResponses
        );

    }

    @Transactional
    public void delete(Long reviewId) {
        PlannerReview plannerReview = plannerReviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        plannerReview = plannerReview.withIsDeleted(true);

        plannerReviewRepository.save(plannerReview);
    }

    @Transactional
    public ReviewPostResponseDto update(Long reviewId, ReviewUpdateRequestDto requestDto, MultipartFile[] images) throws Exception {


        PlannerReview review = plannerReviewRepository.findById(reviewId)
                .orElseThrow(EntityNotFoundException::new);

        LocalDate currentDate = LocalDate.now();    // 현재 날짜
        LocalDate createdAt = review.getCreatedAt();
        long daysBetween = ChronoUnit.DAYS.between(createdAt, currentDate);

        if (daysBetween > 7) {
            throw new RuntimeException("수정할 수 있는 기간이 지났습니다. 7일 이내에만 수정이 가능합니다.");
        }

        // 내용, 별점 수정
        review = review.withContent(requestDto.content());
        review = review.withStars(requestDto.stars());
        plannerReviewRepository.save(review);

        // 이미지 수정
        updateReviewImages(review, images);

        // 태그 수정
        updateReviewTags(review, requestDto);

        return new ReviewPostResponseDto(
                reviewId,
                review.getPlanner().getId(),
                requestDto.content(),
                requestDto.stars(),
                review.getCreatedAt(),
                reviewImageQueryService.getReviewImages(reviewId),
                reviewCategoryQueryService.getReviewCategories(reviewId));
    }

    private List<ReviewCategoryResponseDto> saveCategoriesAndTags(PlannerReview plannerReview, List<ReviewCategoryRequestDto> categoryRequests) {
        return categoryRequests.stream()
                .map(categoryRequest -> {
                    // 카테고리 저장
                    PlannerReviewCategory plannerReviewCategory = saveCategory(plannerReview, categoryRequest);

                    // 태그 저장, responseDTO 생성
                    List<ReviewTagResponseDto> tagResponses = categoryRequest.tags().stream()
                            .map((ReviewTagRequestDto tagRequest) -> saveTag(plannerReviewCategory, tagRequest))
                            .collect(Collectors.toList());

                    // 카테고리 responseDTO 생성
                    return new ReviewCategoryResponseDto(
                            plannerReviewCategory.getId(),
                            plannerReview.getId(),
                            plannerReviewCategory.getCode(),
                            tagResponses
                    );
                }).collect(Collectors.toList());
    }

    // 태그 저장 메서드
    private ReviewTagResponseDto saveTag(PlannerReviewCategory plannerReviewCategory, ReviewTagRequestDto tagRequest) {
        PlannerReviewTag plannerReviewTag = PlannerReviewTag.builder()
                .plannerReviewCategory(plannerReviewCategory)
                .code(tagRequest.code())
                .isDeleted(false)
                .build();

        // 태그 저장
        PlannerReviewTag savedTag = plannerReviewTagRepository.save(plannerReviewTag);

        // 태그 responseDTO 생성&반환
        return new ReviewTagResponseDto(
                savedTag.getId(),
                plannerReviewCategory.getId(),
                savedTag.getCode()
        );
    }

    // 카테고리 저장 메서드
    private PlannerReviewCategory saveCategory(PlannerReview plannerReview, ReviewCategoryRequestDto categoryRequest) {
        PlannerReviewCategory plannerReviewCategory = PlannerReviewCategory.builder()
                .plannerReview(plannerReview)
                .code(categoryRequest.code())
                .build();
        return plannerReviewCategoryRepository.save(plannerReviewCategory);
    }


    private void updateReviewImages(PlannerReview review, MultipartFile[] images) throws Exception {

        // 기존 이미지들 isdeleted=true 처리
        List<PlannerReviewImage> preImages = plannerReviewImageRepository.findByPlannerReviewId(review.getId());
        for (PlannerReviewImage preImage : preImages) {
            preImage = preImage.withIsDeleted(true);
            plannerReviewImageRepository.save(preImage);
        }

        // 새로운 이미지 저장
        reviewImageService.uploadImages(review.getUser().getId(), review.getId(), images);
    }

    private void updateReviewTags(PlannerReview review, ReviewUpdateRequestDto requestDto) {
        // 기존 태그들 isdeleted=true 처리
        List<PlannerReviewCategory> categories = plannerReviewCategoryRepository.findByPlannerReviewId(review.getId());
        categories.forEach(category -> {
            List<PlannerReviewTag> preTags = plannerReviewTagRepository.findByPlannerReviewCategoryId(category.getId());
            preTags.forEach(tag -> {
                tag.withIsDeleted(true);
                plannerReviewTagRepository.save(tag);
            });
        });

        // 새로운 태그 저장
        List<ReviewCategoryRequestDto> categoryRequests = requestDto.categories();
        categoryRequests.forEach( category -> {
            PlannerReviewCategory reviewCategory = PlannerReviewCategory.builder()
                    .code(category.code())
                    .plannerReview(review)
                    .build();

            List<ReviewTagRequestDto> tagRequests = category.tags();
            tagRequests.forEach(tag -> {
                PlannerReviewTag reviewTag = PlannerReviewTag.builder()
                        .code(tag.code())
                        .isDeleted(false)
                        .plannerReviewCategory(reviewCategory)
                        .build();
                plannerReviewTagRepository.save(reviewTag);
            });
        });
    }

    // 태그가 얼마 안되서, 이곳에서 순회하겠습니다.
    public List<?> getCategoryAndTags() {
        List<PlannerReviewCategoryResponse> responses = new ArrayList<>();
        List<PlannerReviewCategory> categories = plannerReviewCategoryRepository.findAll();

        for (PlannerReviewCategory category : categories) {
            List<PlannerReviewTag> tags = plannerReviewTagRepository.findByPlannerReviewCategoryId(category.getId());
            PlannerReviewCategoryResponse response = new PlannerReviewCategoryResponse(
                    category.getCode(),
                    tags
            );
            responses.add(response);
        }

        return responses;
    }
}
