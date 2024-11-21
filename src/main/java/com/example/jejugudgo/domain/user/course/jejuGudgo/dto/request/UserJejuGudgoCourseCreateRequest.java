package com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request;

import java.util.List;

public record UserJejuGudgoCourseCreateRequest(
        String title,
        String content,
        String imageUrl,
        List<UserJejuGudgoCourseSpotCreateRequest> JejuGudgoSpots,
        List<UserJejuGudgoCourseTagCreateRequest> JejuGudgoTags
) {
}
