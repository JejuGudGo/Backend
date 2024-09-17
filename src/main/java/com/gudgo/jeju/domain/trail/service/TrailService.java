package com.gudgo.jeju.domain.trail.service;

import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.trail.dto.response.TrailDetailResponse;
import com.gudgo.jeju.domain.trail.dto.response.TrailRecommendResponse;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.trail.query.TrailQueryService;
import com.gudgo.jeju.domain.trail.repository.TrailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;
    private final ReviewQueryService reviewQueryService;
    private final TrailQueryService trailQueryService;

    public TrailDetailResponse getTrail(Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(EntityNotFoundException::new);

        List<String> images = trailQueryService.findTrailImages(trailId);
        List<TrailRecommendResponse> responses = trailQueryService.findTop5TrailsByMatchingTags(trailId);
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
                responses,
                reviewCount,
                reviews
        );

        return detailResponse;
    }
}
