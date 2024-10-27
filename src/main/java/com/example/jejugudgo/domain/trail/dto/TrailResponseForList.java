package com.example.jejugudgo.domain.trail.dto;

public record TrailResponseForList(
        Long trailId,
        String title,
        double starAvg,
        Long reviewCount
) {
}
