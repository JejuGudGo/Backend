package com.example.jejugudgo.domain.user.myGudgo.profile.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserProfileUpdateRequest(
        String nickname,
        MultipartFile profileImage
) {
}
