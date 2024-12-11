package com.example.jejugudgo.domain.course.search.dto.response;

import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import com.example.jejugudgo.domain.course.common.dto.RoutePoint;

import java.util.List;

public record CoursePathResponse(
        List<RoutePoint> spots,
        List<MapCoordinate> path
) {
}
