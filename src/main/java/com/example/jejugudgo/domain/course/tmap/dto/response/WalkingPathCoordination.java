package com.example.jejugudgo.domain.course.tmap.dto.response;

import javax.annotation.Nullable;

public record WalkingPathCoordination(
       @Nullable String title,
       Long order,
       double latitude,
       double longitude
) {
}
