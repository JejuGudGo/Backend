package com.example.jejugudgo.domain.user.controller;

import com.example.jejugudgo.domain.user.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.UserCheckListResponse;
import com.example.jejugudgo.domain.user.service.UserCheckListService;
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


    @GetMapping("")
    public ResponseEntity<List<UserCheckListResponse>> getAll(HttpServletRequest request){
        return ResponseEntity.ok(userCheckListService.getAll(request));
    }

    @GetMapping("/{checkItemId}")
    public ResponseEntity<UserCheckListResponse> get(@PathVariable("checkItemId") Long checkItemId) {
        return ResponseEntity.ok(userCheckListService.get(checkItemId));
    }

    @PostMapping("")
    public ResponseEntity<UserCheckListResponse> create(@RequestBody UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        UserCheckListResponse userCheckListResponse = userCheckListService.create(createRequest, servletRequest);
        return ResponseEntity.ok(userCheckListResponse);
    }

    @PatchMapping("/{checkItemId}")
    public ResponseEntity<UserCheckListResponse> updateContent(@PathVariable("checkItemId") Long checkItemId, @RequestBody UserCheckListUpdateRequest request) {
        UserCheckListResponse userCheckListResponse = userCheckListService.updateContent(checkItemId, request);
        return ResponseEntity.ok(userCheckListResponse);
    }

    @PatchMapping("/{checkItemId}/finish")
    public ResponseEntity<UserCheckListResponse> updateFinish(@PathVariable("checkItemId") Long checkItemId, @RequestBody UserCheckListUpdateRequest request) {
        UserCheckListResponse userCheckListResponse = userCheckListService.updateFinish(checkItemId, request);
        return ResponseEntity.ok(userCheckListResponse);
    }

}

