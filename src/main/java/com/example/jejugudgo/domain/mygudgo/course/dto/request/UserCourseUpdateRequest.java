package com.example.jejugudgo.domain.mygudgo.course.dto.request;

public record UserCourseUpdateRequest(
        Long id,
        String image,
        String title,
        String content
) {
}
