package com.example.jejugudgo.domain.trail.dto;

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
