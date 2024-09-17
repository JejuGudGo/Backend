package com.gudgo.jeju.domain.post.chat.controller;

import com.gudgo.jeju.domain.post.chat.dto.request.NoticeCreateRequest;
import com.gudgo.jeju.domain.post.chat.dto.request.NoticeUpdateRequest;
import com.gudgo.jeju.domain.post.chat.dto.response.NoticeCreateResponse;
import com.gudgo.jeju.domain.post.chat.dto.response.NoticeResponse;
import com.gudgo.jeju.domain.post.chat.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices")
    public ResponseEntity<NoticeCreateResponse> createNotice(
            @PathVariable("userId") Long userId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestBody NoticeCreateRequest request
            ) {
        NoticeCreateResponse response = noticeService.create(userId, chatRoomId, request);

        return ResponseEntity.ok(response);
    }
}
