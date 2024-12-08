package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import jakarta.annotation.Nullable;

import java.util.List;

public record CourseSearchResponse(
        Long id,
        List<String> tags,
        LikeInfo likeInfo,
        String title,
        @Nullable String route,
        @Nullable String summary,
        @Nullable String distance,
        @Nullable String time,
        String thumbnailUrl,
        @Nullable String starAvg,
        @Nullable Long reviewCount,
        RoutePoint startPoint,
        RoutePoint endPoint
) {
}
