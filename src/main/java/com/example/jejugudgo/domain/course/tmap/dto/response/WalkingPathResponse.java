package com.example.jejugudgo.domain.course.tmap.dto.response;

import javax.annotation.Nullable;
import java.util.List;

public record WalkingPathResponse(
        @Nullable String type,
        String totalTime,
        String totalDistance,
        List<WalkingPathCoordination> walkingPathCoordinations
) {
}
