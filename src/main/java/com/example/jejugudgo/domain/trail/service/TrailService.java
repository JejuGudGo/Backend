package com.example.jejugudgo.domain.trail.service;

import com.example.jejugudgo.domain.trail.dto.TrailDetailResponse;
import com.example.jejugudgo.domain.trail.dto.TrailListResponse;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.entity.TrailType;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.StringHelper.nullIfEmpty;

@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;

    public List<TrailListResponse> getTrails(String query) {
        if (query.equals("전체")) {
            return trailRepository.findAll().stream()
                    .map(trail -> new TrailListResponse(
                            trail.getId(),
                            nullIfEmpty(trail.getImageUrl()),
                            nullIfEmpty(trail.getTitle()),
                            nullIfEmpty(trail.getContent())
                    ))
                    .collect(Collectors.toList());

        } else if (query.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE97);

        } else {
            TrailType trailType = TrailType.fromQuery(query);
            return trailRepository.findByTrailType(trailType).stream()
                    .map(trail -> new TrailListResponse(
                            trail.getId(),
                            nullIfEmpty(trail.getImageUrl()),
                            nullIfEmpty(trail.getTitle()),
                            nullIfEmpty(trail.getContent())
                    ))
                    .collect(Collectors.toList());
        }
    }

    public TrailDetailResponse getTrail(Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        TrailDetailResponse response = new TrailDetailResponse(
                trail.getId(),
                nullIfEmpty(trail.getTitle()),
                trail.getLatitude(),
                trail.getLongitude(),
                nullIfEmpty(trail.getContent()),
                nullIfEmpty(trail.getAddress()),
                nullIfEmpty(trail.getPhoneNumber()),
                nullIfEmpty(trail.getHomepageUrl()),
                nullIfEmpty(trail.getBusinessHours()),
                nullIfEmpty(trail.getFee()),
                nullIfEmpty(trail.getDuration()),
                nullIfEmpty(trail.getImageUrl()),
                nullIfEmpty(trail.getReference()),
                trail.getTrailType().getCode()
        );

        return response;
    }
}
