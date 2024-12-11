package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;

import javax.annotation.Nullable;
import java.util.List;

public record TrailInfoData(
        @Nullable String content,
        RoutePoint startPointInfo,
        @Nullable String address,
        @Nullable String openTime,
        @Nullable String tel,
        @Nullable String fee,
        @Nullable String time,
        @Nullable String hompageUrl,
        @Nullable String thumbnailUrl,
        @Nullable List<SimilarPoint> similarPoints
) {
}
