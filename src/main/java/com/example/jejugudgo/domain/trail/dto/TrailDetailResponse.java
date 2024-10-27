package com.example.jejugudgo.domain.trail.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TrailDetailResponse(
    Long id,
    String title,
    double latitude,
    double longitude,
    String content,
    String address,
    String phoneNumber,
    String homepageUrl,
    String businessHours,
    String fee,
    String duration,
    String imageUrl,
    String reference,
    String trailType
) {
}
