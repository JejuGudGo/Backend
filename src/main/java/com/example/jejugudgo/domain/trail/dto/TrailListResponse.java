package com.example.jejugudgo.domain.trail.dto;

public record TrailListResponse(
        Long trailId,
        String trailImgUrl,
        String title,
        String content
) {
}
