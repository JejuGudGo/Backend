package com.example.jejugudgo.domain.user.controller;

import com.example.jejugudgo.domain.user.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.UserCheckListResponse;
import com.example.jejugudgo.domain.user.service.UserCheckListService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/checklist")
@RequiredArgsConstructor
public class UserCheckListController {
    private final UserCheckListService userCheckListService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getAll(HttpServletRequest request) {
        List<UserCheckListResponse> userCheckListResponseList = userCheckListService.getAll(request);
        return ResponseEntity.ok(apiResponseUtil.success(userCheckListResponseList));
    }

    @GetMapping("/{checkItemId}")
    public ResponseEntity<CommonApiResponse> get(@PathVariable("checkItemId") Long checkItemId, HttpServletRequest request) {
        UserCheckListResponse userCheckListResponse =  userCheckListService.get(checkItemId);
        return ResponseEntity.ok(apiResponseUtil.success(userCheckListResponse));
    }

    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(@RequestBody UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        UserCheckListResponse userCheckListResponse = userCheckListService.create(createRequest, servletRequest);
        return ResponseEntity.ok(apiResponseUtil.success(userCheckListResponse));
    }

    @PatchMapping("/{checkItemId}")
    public ResponseEntity<CommonApiResponse> updateContent(@PathVariable("checkItemId") Long checkItemId, @RequestBody UserCheckListUpdateRequest request, HttpServletRequest servletRequest) {
        UserCheckListResponse userCheckListResponse = userCheckListService.updateCheckList(checkItemId, request);
        return ResponseEntity.ok(apiResponseUtil.success(userCheckListResponse));
    }

    @PatchMapping("/{checkItemId}/reorder")
    public ResponseEntity<CommonApiResponse> updateOrderNumber(@PathVariable("checkItemId") Long checkItemId, @RequestBody UserCheckListUpdateRequest request, HttpServletRequest servletRequest) {
        UserCheckListResponse userCheckListResponse = userCheckListService.updateCheckList(checkItemId, request);
        return ResponseEntity.ok(apiResponseUtil.success(userCheckListResponse));
    }

    @DeleteMapping("/{checkItemId}")
    public ResponseEntity<CommonApiResponse> delete(@PathVariable("checkItemId") Long checkItemId) {
        userCheckListService.delete(checkItemId);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}

