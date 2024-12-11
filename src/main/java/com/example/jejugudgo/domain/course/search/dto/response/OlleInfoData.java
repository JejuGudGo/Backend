package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;

import javax.annotation.Nullable;
import java.util.List;

public record OlleInfoData(
        List<RoutePoint> routes,
        String content,
        RoutePoint startPointInfo,
        RoutePoint endPointInfo,
        @Nullable String address,
        @Nullable String openTime,
        @Nullable String tel
) {
}
