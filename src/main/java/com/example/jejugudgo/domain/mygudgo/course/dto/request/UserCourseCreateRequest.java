package com.example.jejugudgo.domain.mygudgo.course.dto.request;

import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;

import java.util.List;

public record UserCourseCreateRequest(
        String title,
        List<String> tag,
        String content,
        List<SpotInfo> spotInfo
) {
}
