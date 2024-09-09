package com.gudgo.jeju.domain.post.chat.service;

import com.gudgo.jeju.domain.post.chat.dto.request.ChatRoomUpdateRequest;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.post.chat.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public void updateMessageRoomTitle(Long chatRoomId, ChatRoomUpdateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(EntityNotFoundException::new);

        chatRoom = chatRoom.withTitle(request.title());

        chatRoomRepository.save(chatRoom);
    }
}
