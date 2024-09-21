package com.gudgo.jeju.domain.trail.service;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;
import com.gudgo.jeju.domain.review.dto.response.TopRatingTagResponseDto;
import com.gudgo.jeju.domain.review.entity.Review;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.review.repository.ReviewRepository;
import com.gudgo.jeju.domain.trail.dto.response.TrailDetailResponse;
import com.gudgo.jeju.domain.trail.dto.response.TrailRecommendResponse;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.trail.query.TrailQueryService;
import com.gudgo.jeju.domain.trail.repository.TrailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;
    private final ReviewQueryService reviewQueryService;
    private final TrailQueryService trailQueryService;
    private final ReviewRepository reviewRepository;

    public TrailDetailResponse getTrail(Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(EntityNotFoundException::new);

        List<TopRatingTagResponseDto> topRatingTags = reviewQueryService.findTrailTop5ReviewTags(trailId);
        List<String> images = trailQueryService.findTrailImages(trailId);
        List<TrailRecommendResponse> recommends = trailQueryService.findTop5TrailsByMatchingTags(trailId);
        Long reviewCount = reviewQueryService.getTrailReviewCount(trailId);
        List<ReviewResponse> reviews = reviewQueryService.getTrailReview(trailId);

        TrailDetailResponse detailResponse = new TrailDetailResponse(
                trail.getTitle(),
                trail.getAvgStar(),
                images,
                trail.getSummary(),
                trail.getAddress(),
                trail.getTime(),
                trail.getContent(),
                trail.getTel(),
                trail.getLatitude(),
                trail.getLongitude(),
                topRatingTags,
                recommends,
                reviewCount,
                reviews
        );

        return detailResponse;
    }

    @Transactional
    public void updateAllOriginalTrailStarAvg() {
        List<Trail> trails = trailRepository.findAll();
        for (Trail trail : trails) {
            List<Review> reviews = reviewRepository.findByTrailIdNotNull(trail.getId());

            if (!reviews.isEmpty()) {
                OptionalDouble avgStars = reviews.stream()
                        .mapToDouble(Review::getStars)
                        .average();

                if (avgStars.isPresent()) {
                    Trail updatedTrail = trail.withStarAvg(avgStars.getAsDouble());
                    trailRepository.save(updatedTrail);
                }
            }
        }
    }
}
