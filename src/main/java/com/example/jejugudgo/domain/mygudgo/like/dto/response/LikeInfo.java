package com.example.jejugudgo.domain.mygudgo.like.dto.response;

import jakarta.annotation.Nullable;

public record LikeInfo(
        boolean isLiked,
        @Nullable String courseType,
        @Nullable Long id
) {
}
