package com.example.jejugudgo.domain.review.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseService;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.course.olle.service.JejuOlleCourseService;
import com.example.jejugudgo.domain.review.message.ReviewPublisher;
import com.example.jejugudgo.domain.review.util.StarAvgCalculator;
import com.example.jejugudgo.domain.review.dto.request.ReviewCreateRequest;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import com.example.jejugudgo.domain.review.repository.ReviewRepository;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.domain.trail.service.TrailService;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final TokenUtil tokenUtil;
    private final StarAvgCalculator starAvgCalculator;

    private final ReviewCategoryService reviewCategoryService;
    private final ReviewImageService reviewImageService;
    private final ReviewPublisher reviewPublisher;
    private final JejuGudgoCourseService jejuGudgoCourseService;
    private final JejuOlleCourseService jejuOlleCourseService;
    private final TrailService trailService;

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final JejuOlleCourseRepository olleCourseRepository;
    private final TrailRepository trailRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;




    @Transactional
    public void createReview(String type, Long courseId, ReviewCreateRequest request, List<MultipartFile> images, HttpServletRequest httpServletRequest) throws Exception {
        User user = getUser(httpServletRequest);

        // 1. 리뷰 생성
        Review review = createReview(type, courseId, request);

        // 2. 리뷰 카테고리 생성
        List<ReviewCategory> reviewCategories = reviewCategoryService.createReviewCategory(request.reviewCategory1(), request.reviewCategory2(), request.reviewCategory3(), review);

        // 3. 리뷰 이미지 생성
        reviewImageService.createReviewImages(images, user.getId(), review);

        // 4. 메세지 큐
        reviewPublisher.createReviewMessagePublish(review, reviewCategories);
    }

    private Review createReview(String reviewType, Long courseId, ReviewCreateRequest request) {
        JejuGudgoCourse jejuGudgoCourse = null;
        JejuOlleCourse olleCourse = null;
        Trail trail = null;
        int currentSize = 0;
        double newStarAvg = 0.0;

        if (reviewType.equals("jejugudgo")) {
            jejuGudgoCourse = jejuGudgoCourseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            currentSize = reviewRepository.findDistinctUserByJejuGudgoCourse(jejuGudgoCourse).size();
            newStarAvg = starAvgCalculator.validateCourseByType(jejuGudgoCourse.getStarAvg(), currentSize, request.stars());
            jejuGudgoCourseService.updateStarAvg(newStarAvg, jejuGudgoCourse);

        } else if (reviewType.equals("olle")) {
            olleCourse = olleCourseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            currentSize = reviewRepository.findDistinctUserByJejuOlleCourse(olleCourse).size();
            newStarAvg = starAvgCalculator.validateCourseByType(olleCourse.getStarAvg(), currentSize, request.stars());
            jejuOlleCourseService.updateStarAvg(newStarAvg, olleCourse);

        } else if (reviewType.equals("trail")) {
            trail = trailRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

            currentSize = reviewRepository.findDistinctUserByTrail(trail).size();
            newStarAvg = starAvgCalculator.validateCourseByType(trail.getStarAvg(), currentSize, request.stars());
            trailService.updateStarAvg(newStarAvg, trail);

        } else
            throw new CustomException(RetCode.RET_CODE94);

        Review review = Review.builder()
                .jejuGudgoCourse(jejuGudgoCourse)
                .jejuOlleCourse(olleCourse)
                .trail(trail)
                .finishedAt(request.finishedAt())
                .createdAt(LocalDate.now())
                .content(request.content())
                .stars(request.stars())
                .build();

        reviewRepository.save(review);

        return review;
    }

    private User getUser(HttpServletRequest httpServletRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpServletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return user;
    }
}
