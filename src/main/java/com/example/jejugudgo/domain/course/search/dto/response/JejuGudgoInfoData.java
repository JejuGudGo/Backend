package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;

import javax.annotation.Nullable;
import java.util.List;

public record JejuGudgoInfoData(
        List<RoutePoint> routes,
        @Nullable String content,
        RoutePoint startPointInfo,
        RoutePoint endPointInfo
) {
}
