package com.example.jejugudgo.domain.trail.dto;

import com.example.jejugudgo.domain.search.dto.sub.TopFiveRankedKeywordResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TrailDetailResponse(
    Long id,
    String title,
    double latitude,
    double longitude,
    boolean isBookmarked,
    String content,
    String address,
    String phoneNumber,
    String homepageUrl,
    String businessHours,
    String fee,
    String duration,
    String imageUrl,
    String reference,
    String trailType,
    List<TopFiveRankedKeywordResponse> keywords
) {
}
