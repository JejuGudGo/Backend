package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;

import javax.annotation.Nullable;
import java.util.List;

public record BasicData(
        Long id,
        @Nullable List<String> tags,
        LikeInfo likeInfo,
        String title,
        @Nullable String route,
        @Nullable String distance,
        @Nullable String time,
        @Nullable Double starAvg,
        @Nullable Long reviewCount
) {
}
