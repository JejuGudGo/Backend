package com.example.jejugudgo.domain.profile.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserProfileUpdateRequest(
        String nickname,
        MultipartFile profileImage
) {
}
