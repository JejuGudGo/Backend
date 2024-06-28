package com.gudgo.jeju.domain.planner.controller;

import com.gudgo.jeju.domain.planner.dto.request.chatting.MessagePaginationRequest;
import com.gudgo.jeju.domain.planner.dto.request.chatting.MessageRequest;
import com.gudgo.jeju.domain.planner.dto.response.MessageResponse;
import com.gudgo.jeju.domain.planner.query.MessageQueryService;
import com.gudgo.jeju.domain.planner.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageQueryService messageQueryService;
    private final MessageService messageService;

    @MessageMapping(value = "/chatRooms/{chatRoomId}/send")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload MessageRequest messageRequest,
            MultipartFile[] images
    ) throws Exception {
        MessageResponse messageResponse = messageService.sendMessage(chatRoomId, images, messageRequest);

        simpMessagingTemplate.convertAndSend("/sub/chatRooms/" + chatRoomId,
                Map.of("current-chat", messageResponse));
    }


    @MessageMapping(value = "/chatRooms/{chatRoomId}/before/{lastChatId}")
    public void getMessages(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @DestinationVariable("lastChatId") Long lastChatId,
            @Payload MessagePaginationRequest request
    ) {
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<MessageResponse> messageResponses = messageQueryService.findMessagesByChatRoomIdAndMaxIndex(chatRoomId, lastChatId, pageable);

        simpMessagingTemplate.convertAndSend("/sub/chatRooms/" + chatRoomId,
                Map.of("before-chat", messageResponses));
    }
}
