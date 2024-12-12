package com.example.jejugudgo.domain.mygudgo.course.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserCourseUpdateRequest(
        Long id,
        MultipartFile image,
        String title,
        String content
) {
}
