package com.gudgo.jeju.domain.post.chat.query;

import com.gudgo.jeju.domain.post.chat.dto.response.MessageResponse;
import com.gudgo.jeju.domain.post.chat.entity.Message;
import com.gudgo.jeju.domain.planner.entity.QMessage;
import com.gudgo.jeju.domain.planner.entity.QMessageImage;
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
public class MessageQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MessageQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<MessageResponse> findMessagesByChatRoomIdAndMaxIndex(Long chatRoomId, Long maxIndex, Pageable pageable) {
        QMessage qMessage = QMessage.message;
        QMessageImage qMessageImage = QMessageImage.messageImage;

        List<Message> messages = queryFactory
                .selectFrom(qMessage)
                .where(qMessage.chatRoom.id.eq(chatRoomId)
                        .and(qMessage.id.lt(maxIndex)))
                .orderBy(qMessage.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MessageResponse> responses = messages.stream()
                .map(message -> {
                    List<String> images = queryFactory
                            .select(qMessageImage.messageImageUrl)
                            .from(qMessageImage)
                            .where(qMessageImage.message.id.eq(message.getId()))
                            .fetch();

                    return new MessageResponse(
                            message.getParticipant().getUser().getId(),
                            message.getParticipant().getUser().getNickname(),
                            message.getParticipant().getUser().getNumberTag(),
                            message.getParticipant().getUser().getProfile().getProfileImageUrl(),
                            message.getContent(),
                            images.isEmpty() ? null : images
                    );
                })
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(qMessage)
                .where(qMessage.chatRoom.id.eq(chatRoomId)
                        .and(qMessage.id.lt(maxIndex)))
                .fetchCount();

        return new PageImpl<>(responses, pageable, total);
    }
}
