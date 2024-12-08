package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;

public record SimilarPoint(
        Long id,
        LikeInfo likeInfo,
        String title,
        String courseType,
        String thumbnailUrl
) {
}
