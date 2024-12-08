package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;

import java.util.List;

public record CourseSearchResponse(
        Long id,
        String cat1,
        List<String> tags,
        LikeInfo likeInfo,
        String title,
        @Nullable String address,
        @Nullable String route,
        @Nullable String summary,
        @Nullable String content,
        @Nullable String distance,
        @Nullable String time,
        String thumbnailUrl,
        @Nullable Double starAvg,
        @Nullable Long reviewCount,
        @Nullable Long likeCount,
        @Nullable Long clickCount,
        @Nullable Double upToDate,
        RoutePoint startPoint,
        @Nullable RoutePoint endPoint
) {
}
