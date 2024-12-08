package com.example.jejugudgo.domain.mygudgo.like.controller;

import com.example.jejugudgo.domain.mygudgo.like.dto.request.UserLikeRequest;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.UserLikeResponse;
import com.example.jejugudgo.domain.mygudgo.like.service.UserLikeService;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import com.example.jejugudgo.global.util.paging.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user/like/")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserLikeService userLikeService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getUserLikes(
            @RequestParam("type") String query,
            HttpServletRequest request,
            Pageable pageable) {
        Pageable page = PagingUtil.createPageable(pageable.getPageNumber(), pageable.getPageSize());
        List<UserLikeResponse> response = userLikeService.getUserLikes(query, request, page);
        return ResponseEntity.ok(apiResponseUtil.success(response, "userLikes"));
    }

    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest servletRequest,@RequestBody UserLikeRequest userLikeRequest) {
        UserLikeResponse response = userLikeService.create(servletRequest, userLikeRequest);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @DeleteMapping("")
    public ResponseEntity<CommonApiResponse> delete(@RequestParam("userLikeId") Long userLikeId, HttpServletRequest request) {
        userLikeService.delete(userLikeId, request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
