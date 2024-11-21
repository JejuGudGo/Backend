package com.example.jejugudgo.domain.course.jejugudgo.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record JejuGudgoCourseUpdateRequest(
        MultipartFile image,
        String title,
        String content
) {
}
