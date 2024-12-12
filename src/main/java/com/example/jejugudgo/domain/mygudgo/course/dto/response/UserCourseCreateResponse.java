package com.example.jejugudgo.domain.mygudgo.course.dto.response;

import java.util.List;

public record UserCourseCreateResponse(
        Long id,
        String title,
        List<String> tag,
        String content,
        List<SpotInfo> spotInfo
) {
}
