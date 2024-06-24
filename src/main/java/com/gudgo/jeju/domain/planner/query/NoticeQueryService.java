package com.gudgo.jeju.domain.planner.query;

import com.gudgo.jeju.domain.planner.dto.response.NoticeResponse;
import com.gudgo.jeju.domain.planner.entity.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeQueryService {
    private JPAQueryFactory queryFactory;

    @Autowired
    public NoticeQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 페이징 처리 or limit
    public List<NoticeResponse> getNotices(Long chatRoomId) {
        QNotice qNotice = QNotice.notice;

        List<Notice> notices = queryFactory
                .selectFrom(qNotice)
                .where(qNotice.isDeleted.isFalse()
                        .and(qNotice.chatRoom.id.eq(chatRoomId)))
                .orderBy(qNotice.createdAt.asc())
                .fetch();

        List<NoticeResponse> noticeResponses = notices.stream()
                .map(notice ->
                        new NoticeResponse(
                                notice.getId(),
                                notice.getParticipant().getId(),
                                notice.getParticipant().getUser().getNickname(),
                                notice.getParticipant().getUser().getNumberTag(),
                                notice.getParticipant().getUser().getProfile().getProfileImageUrl(),
                                notice.getContent(),
                                notice.getCreatedAt(),
                                notice.getUpdatedAt()
                        )).toList();

        return noticeResponses;
    }

    public NoticeResponse getLatestNotice(Long chatRoomId) {
        QNotice qNotice = QNotice.notice;

        Notice notice = queryFactory
                .selectFrom(qNotice)
                .where(qNotice.isDeleted.isFalse()
                        .and(qNotice.chatRoom.id.eq(chatRoomId)))
                .orderBy(qNotice.createdAt.desc())
                .limit(1)
                .fetchOne();

        return new NoticeResponse(
                notice.getId(),
                notice.getParticipant().getId(),
                notice.getParticipant().getUser().getNickname(),
                notice.getParticipant().getUser().getNumberTag(),
                notice.getParticipant().getUser().getProfile().getProfileImageUrl(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
