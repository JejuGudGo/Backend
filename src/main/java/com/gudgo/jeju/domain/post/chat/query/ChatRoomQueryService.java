package com.gudgo.jeju.domain.post.chat.query;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomListResponse;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomResponse;
import com.gudgo.jeju.domain.post.chat.dto.response.ChattingUserResponse;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.post.chat.entity.QChatRoom;
import com.gudgo.jeju.domain.post.chat.entity.QMessage;
import com.gudgo.jeju.domain.post.participant.entity.QParticipant;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ChatRoomQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<ChatRoomListResponse> getChatRooms (Long userId, Pageable pageable) {
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QMessage qMessage = QMessage.message;
        QParticipant qParticipant = QParticipant.participant;

        List<ChatRoomListResponse> chatRooms = queryFactory
                .select(qChatRoom.id, qChatRoom.title)
                .from(qChatRoom)
                .leftJoin(qParticipant).on(qParticipant.planner.chatRoom.eq(qChatRoom))
                .where(qParticipant.user.id.eq(userId)
                        .and(qParticipant.isDeleted.isFalse())
                        .and(qChatRoom.id.isNotNull()))
                .fetch()
                .stream()
                .map(tuple -> {
                    Long chatRoomId = tuple.get(qChatRoom.id);
                    String chatRoomTitle = tuple.get(qChatRoom.title);

                    List<String> profileImages = queryFactory
                            .select(qParticipant.planner.user.profile.profileImageUrl)
                            .from(qParticipant)
                            .where(qParticipant.planner.chatRoom.id.eq(chatRoomId))
                            .fetch();

                    String recentMessage = queryFactory
                            .select(qMessage.content)
                            .from(qMessage)
                            .where(qMessage.chatRoom.id.eq(chatRoomId))
                            .orderBy(qMessage.createdAt.desc())
                            .limit(1)
                            .fetchOne();

                    LocalDateTime recentMessageDate = queryFactory
                            .select(qMessage.createdAt)
                            .from(qMessage)
                            .where(qMessage.chatRoom.id.eq(chatRoomId))
                            .orderBy(qMessage.createdAt.desc())
                            .limit(1)
                            .fetchOne();

                    return new ChatRoomListResponse(
                            chatRoomId,
                            chatRoomTitle,
                            profileImages,
                            recentMessage,
                            recentMessageDate
                    );
                })
                .toList();
        return PaginationUtil.listToPage(chatRooms, pageable);
    }

    public ChatRoomResponse getChatRoom(Long chatRoomId) {
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QMessage qMessage = QMessage.message;
        QParticipant qParticipant = QParticipant.participant;
        QPlanner qPlanner = QPlanner.planner;

        ChatRoom chatRoom = queryFactory
                .selectFrom(qChatRoom)
                .where(qChatRoom.id.eq(chatRoomId))
                .fetchOne();

        if (chatRoom == null) {
            throw new RuntimeException("Chat room not found");
        }

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.chatRoom.eq(chatRoom))
                .fetchOne();

        List<ChattingUserResponse> participants = queryFactory
                .select(qParticipant.user.id, qParticipant.user.profile.profileImageUrl, qParticipant.isHost, qParticipant.user.nickname)
                .from(qParticipant)
                .where(qParticipant.planner.chatRoom.id.eq(chatRoomId))
                .fetch()
                .stream()
                .map(tuple -> new ChattingUserResponse(
                        tuple.get(qParticipant.user.id),
                        tuple.get(qParticipant.user.profile.profileImageUrl),
                        Boolean.TRUE.equals(tuple.get(qParticipant.isHost)),
                        tuple.get(qParticipant.user.nickname)
                ))
                .collect(Collectors.toList());

        List<String> recentMessages = queryFactory
                .select(qMessage.content)
                .from(qMessage)
                .where(qMessage.chatRoom.id.eq(chatRoomId))
                .orderBy(qMessage.createdAt.desc())
                .limit(100)
                .fetch();

        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getTitle(),
                participants.size(), // 참가자 수
                planner.getCourse().getCreatedAt(), // Planner의 생성 일자가 채팅방의 개설 일자
                participants,
                recentMessages
        );
    }
}
