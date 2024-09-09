package com.gudgo.jeju.domain.post.chat.controller;

import com.gudgo.jeju.domain.post.chat.dto.request.MessagePaginationRequest;
import com.gudgo.jeju.domain.post.chat.dto.request.MessageRequest;
import com.gudgo.jeju.domain.post.chat.dto.response.MessageResponse;
import com.gudgo.jeju.domain.post.chat.query.MessageQueryService;
import com.gudgo.jeju.domain.post.chat.service.MessageService;
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

import java.io.*;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageQueryService messageQueryService;
    private final MessageService messageService;

    @MessageMapping("/chatRooms/{chatRoomId}/send")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload MessageRequest messageRequest
    ) throws Exception {
        List<MultipartFile> multipartFiles = new ArrayList<>();

        if (messageRequest.images() != null) {
            for (Map<String, String> image : messageRequest.images()) {
                String name = image.get("name");
                String content = image.get("content");
                byte[] decodedBytes = Base64.getDecoder().decode(content.split(",")[1]);

                MultipartFile multipartFile = new Base64DecodedMultipartFile(decodedBytes, "application/octet-stream", name);

                multipartFiles.add(multipartFile);
            }
        }

        MultipartFile[] imageFiles = multipartFiles.toArray(new MultipartFile[0]);
        MessageResponse messageResponse = messageService.sendMessage(chatRoomId, imageFiles, messageRequest);

        simpMessagingTemplate.convertAndSend("/sub/chatRooms/" + chatRoomId,
                Map.of("current-chat", messageResponse));
    }

    // 이전 채팅 기록 내역
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

    static class Base64DecodedMultipartFile implements MultipartFile {

        private final byte[] content;
        private final String header;
        private final String filename;

        public Base64DecodedMultipartFile(byte[] content, String header, String filename) {
            this.content = content;
            this.header = header;
            this.filename = filename;
        }

        @Override
        public String getName() {
            return filename;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return header.split(":")[1];
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }
}