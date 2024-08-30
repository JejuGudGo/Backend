package com.gudgo.jeju.domain.post.chat.controller;

import com.gudgo.jeju.domain.post.chat.dto.request.NoticeCreateRequest;
import com.gudgo.jeju.domain.post.chat.dto.request.NoticeUpdateRequest;
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

    @GetMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices")
    public ResponseEntity<List<NoticeResponse>> getNotices(@PathVariable("userId") Long userId, @PathVariable("chatRoomId") Long chatRoomId) {
        List<NoticeResponse> responses = noticeService.getNotices(chatRoomId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices/latest")
    public ResponseEntity<NoticeResponse> getLatestNotice(@PathVariable("userId") Long userId, @PathVariable("chatRoomId") Long chatRoomId) {
        NoticeResponse response = noticeService.getLatestNotice(chatRoomId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices")
    public ResponseEntity<NoticeResponse> create(
            @PathVariable("userId") Long userId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestBody NoticeCreateRequest request
            ) {
        NoticeResponse response = noticeService.create(userId, chatRoomId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices/{noticeId}")
    public ResponseEntity<NoticeResponse> update(
            @PathVariable("userId") Long userId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @PathVariable("noticeId") Long noticeId,
            @RequestBody NoticeUpdateRequest request
    ) {
        NoticeResponse response = noticeService.update(noticeId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{userId}/chatRooms/{chatRoomId}/notices/{noticeId}")
    public ResponseEntity<?> update(
            @PathVariable("userId") Long userId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @PathVariable("noticeId") Long noticeId
    ) {
        noticeService.delete(noticeId);

        return ResponseEntity.ok().build();
    }
}
