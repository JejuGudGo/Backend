package com.gudgo.jeju.domain.post.chat.controller;

import com.gudgo.jeju.domain.post.chat.dto.request.ChatRoomUpdateRequest;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomListResponse;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomResponse;
import com.gudgo.jeju.domain.post.chat.query.ChatRoomQueryService;
import com.gudgo.jeju.domain.post.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatRoomController {
    private ChatRoomService chatRoomService;
    private ChatRoomQueryService queryService;

    @GetMapping(value = "/users{userId}/chatRooms")
    public Page<ChatRoomListResponse> getChatRooms(@PathVariable("userId") Long userId, Pageable pageable) {
        Page<ChatRoomListResponse> responses = queryService.getChatRooms(userId, pageable);

        return responses;
    }

    @GetMapping(value = "/chatRooms/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        ChatRoomResponse response = queryService.getChatRoom(chatRoomId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/chatRooms/{chatRoomId}")
    public ResponseEntity<?> updateChatRooms(
            @PathVariable("chatRoomId") Long chatRoomId, @RequestBody ChatRoomUpdateRequest request) {
        chatRoomService.updateMessageRoomTitle(chatRoomId, request);

        return ResponseEntity.ok().build();
    }
}
