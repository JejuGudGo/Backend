package com.example.jejugudgo.domain.trail.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TrailListResponse(
        Long trailId,
        String trailImgUrl,
        String title,
        String content
) {
}
