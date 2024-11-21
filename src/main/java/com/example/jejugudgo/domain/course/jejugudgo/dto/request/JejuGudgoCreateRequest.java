package com.example.jejugudgo.domain.course.jejugudgo.dto.request;

import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;

import java.util.List;

public record JejuGudgoCreateRequest(
        UserJejuGudgoCourse userJejuGudgoCourse,
        List<UserJejuGudgoCourseSpot> jejuGudgoCourseSpots,
        List<UserJejuGudgoCourseTag> jejuGudgoCourseTags
) {
}
