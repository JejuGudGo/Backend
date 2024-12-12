package com.example.jejugudgo.domain.mygudgo.course.dto.response;

import java.util.List;

public record UserCourseUpdateResponse(
        Long id,
        String title,
        List<String> tags,
        String content,
        String route,
        String image
) {
}
