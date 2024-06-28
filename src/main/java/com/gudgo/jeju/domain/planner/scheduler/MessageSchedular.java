package com.gudgo.jeju.domain.planner.scheduler;

import com.gudgo.jeju.domain.planner.cache.MessageCaching;
import com.gudgo.jeju.domain.planner.dto.response.MessageResponse;
import com.gudgo.jeju.domain.planner.entity.*;
import com.gudgo.jeju.domain.planner.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.repository.ChatRoomRepository;
import com.gudgo.jeju.domain.planner.repository.MessageImageRepository;
import com.gudgo.jeju.domain.planner.repository.MessageLogRepository;
import com.gudgo.jeju.domain.planner.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// TODO: 나중에 로그 테이블 합치기
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSchedular {
    private final MessageCaching messageCaching;
    private final ParticipantQueryService participantQueryService;
    private final MessageRepository messageRepository;
    private final MessageLogRepository messageLogRepository;
    private final MessageImageRepository messageImageRepository;
    private final ChatRoomRepository chatRoomRepository;


    // 5분 마다 저장
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void schedule() {
        List<String> keys = messageCaching.getAllChatKeys();

        for (String key : keys) {
            key = key.substring(5);
            Long chatRoomId = Long.valueOf(key);

            List<MessageResponse> responses = messageCaching.getAllMessagesFromRedis(chatRoomId);

            for (MessageResponse response : responses) {
                try {
                    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                            .orElseThrow(EntityNotFoundException::new);

                    Participant participant = participantQueryService.findParticipantIdByChatRoomIdAndUserId(chatRoomId, response.userId());

                    Message message = Message.builder()
                            .chatRoom(chatRoom)
                            .participant(participant)
                            .content(response.message())
                            .build();

                    messageRepository.save(message);

                    if (!response.images().isEmpty()) {
                        for (String image : response.images()) {
                            MessageImage messageImage = MessageImage.builder()
                                    .message(message)
                                    .messageImageUrl(image)
                                    .build();

                            messageImageRepository.save(messageImage);
                        }
                    }

                } catch (Exception e) {
                    MessageLog log = MessageLog.builder()
                            .chatRoomId(chatRoomId)
                            .userId(response.userId())
                            .status(LogStatus.FAILURE)
                            .errorMessage(e.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build();

                    messageLogRepository.save(log);

                    System.err.println("Failed to process message for chat room ID " + chatRoomId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            messageCaching.deleteMessageFromRedis(chatRoomId);
        }
    }
}
