package com.gudgo.jeju.domain.post.chat.controller;

import com.gudgo.jeju.domain.post.chat.dto.request.ChatRoomUpdateRequest;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomResponse;
import com.gudgo.jeju.domain.post.chat.query.ChatRoomQueryService;
import com.gudgo.jeju.domain.post.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ChatRoomController {
    private ChatRoomService chatRoomService;
    private ChatRoomQueryService queryService;

    @GetMapping(value = "{userId}/chatRooms")
    public Page<ChatRoomResponse> getChatRooms(@PathVariable("userId") Long userId, Pageable pageable) {
        Page<ChatRoomResponse> responses = queryService.findChatRoomsByUserId(userId, pageable);

        return responses;
    }

    @PatchMapping(value = "{userId}/chatRooms/{chatRoomId}")
    public ResponseEntity<?> updateChatRooms(
            @PathVariable("userId") Long userId, @PathVariable("chatRoomId") Long chatRoomId, @RequestBody ChatRoomUpdateRequest request) {
        chatRoomService.updateMessageRoomTitle(chatRoomId, request);

        return ResponseEntity.ok().build();
    }
}
