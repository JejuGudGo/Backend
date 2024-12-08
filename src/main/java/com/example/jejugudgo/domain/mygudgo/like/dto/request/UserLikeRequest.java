package com.example.jejugudgo.domain.mygudgo.like.dto.request;

public record UserLikeRequest(
        Long targetId,
        String courseType
) {
}
