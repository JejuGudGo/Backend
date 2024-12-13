package com.example.jejugudgo.domain.course.common.service;

import com.example.jejugudgo.domain.course.common.dto.response.TrailListResponse;
import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.repository.TrailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TrailService {

    private final TrailRepository trailRepository;

    public TrailListResponse getTrailForList(Long targetId) {
        Trail trail = trailRepository.findById(targetId).orElse(null);
        if (trail != null) {
            return new TrailListResponse(
                    trail.getId(),
                    trail.getTrailTag().getTag(),
                    trail.getTitle(),
                    trail.getContent(),
                    trail.getThumbnailUrl(),
                    trail.getStarAvg(),
                    trail.getReviewCount()
            );
        }

        return null;
    }
}

