package com.gudgo.jeju.domain.post.chat.query;

import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.post.chat.dto.response.ChatRoomResponse;
import com.gudgo.jeju.domain.post.chat.entity.QChatRoom;
import com.gudgo.jeju.domain.post.participant.entity.QParticipant;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<ChatRoomResponse> findChatRoomsByUserId(Long userId, Pageable pageable) {
        QPlanner qPlanner = QPlanner.planner;
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QParticipant qParticipant = QParticipant.participant;
        QUser qUser = QUser.user;

        List<ChatRoomResponse> chatRooms = queryFactory
                .select(Projections.constructor(ChatRoomResponse.class,
                        qChatRoom.id,
                        qChatRoom.title,
                        Projections.list(qUser.profile.profileImageUrl)
                ))
                .from(qPlanner)
                .join(qPlanner.chatRoom, qChatRoom)
                .join(qParticipant.planner, qPlanner)
                .where(qPlanner.user.id.eq(userId))
                .fetch()
                .stream()
                .map(chatRoom -> new ChatRoomResponse(
                        chatRoom.chatRoomId(),
                        chatRoom.title(),
                        chatRoom.profileImages().stream().distinct().collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(chatRooms, pageable, chatRooms.size());
    }
}
