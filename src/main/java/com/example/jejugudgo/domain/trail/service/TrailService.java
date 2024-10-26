package com.example.jejugudgo.domain.trail.service;

import com.example.jejugudgo.domain.trail.dto.TrailDetailResponse;
import com.example.jejugudgo.domain.trail.dto.TrailListResponse;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.entity.TrailType;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;

    public List<TrailListResponse> getTrails(String query) {
        if (query.equals("전체")) {
            return trailRepository.findAll().stream()
                    .map(trail -> new TrailListResponse(
                            trail.getId(),
                            trail.getImageUrl(),
                            trail.getTitle(),
                            trail.getContent()
                    ))
                    .collect(Collectors.toList());

        } else if (query.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE97);

        } else {
            TrailType trailType = TrailType.fromCode(query);
            return trailRepository.findByTrailType(trailType).stream()
                    .map(trail -> new TrailListResponse(
                            trail.getId(),
                            trail.getImageUrl(),
                            trail.getTitle(),
                            trail.getContent()
                    ))
                    .collect(Collectors.toList());
        }
    }

    public TrailDetailResponse getTrail(Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        TrailDetailResponse response = new TrailDetailResponse(
                trail.getId(),
                trail.getTitle(),
                trail.getLatitude(),
                trail.getLongitude(),
                trail.getContent(),
                trail.getAddress(),
                trail.getPhoneNumber(),
                trail.getHomepageUrl(),
                trail.getBusinessHours(),
                trail.getFee(),
                trail.getDuration(),
                trail.getImageUrl(),
                trail.getReference(),
                trail.getTrailType().getCode()
        );

        return response;
    }
}
