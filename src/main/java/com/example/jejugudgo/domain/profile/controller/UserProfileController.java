package com.example.jejugudgo.domain.profile.controller;

import com.example.jejugudgo.domain.profile.dto.request.UserProfileUpdateRequest;
import com.example.jejugudgo.domain.profile.dto.response.UserProfileUpdateResponse;
import com.example.jejugudgo.domain.profile.service.UserProfileService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> get(HttpServletRequest request) {
        UserProfileUpdateResponse userProfileUpdateResponse = userProfileService.getProfileForUpdate(request);
        return ResponseEntity.ok(apiResponseUtil.success(userProfileUpdateResponse));
    }


    @PatchMapping("")
    public ResponseEntity<CommonApiResponse> updateProfile(
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest servletRequest) throws Exception {
        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest(nickname, profileImage);
        UserProfileUpdateResponse userProfileResponse = userProfileService.updateProfile(updateRequest, servletRequest);
        return ResponseEntity.ok(apiResponseUtil.success(userProfileResponse));
    }

}
