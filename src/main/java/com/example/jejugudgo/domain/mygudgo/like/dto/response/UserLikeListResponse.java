package com.example.jejugudgo.domain.mygudgo.like.dto.response;

import com.example.jejugudgo.domain.course.common.dto.response.CourseListResponse;
import com.example.jejugudgo.domain.course.common.dto.response.TrailListResponse;
import jakarta.annotation.Nullable;

public record UserLikeListResponse(
        @Nullable Long id,
        CourseListResponse courseForList,
        TrailListResponse trailForList
) {
}
