package com.gudgo.jeju.domain.post.chat.dto.response;

import java.time.LocalDateTime;

public record NoticeResponse (
        Long noticeId,
        Long participantId,
        String nickname,
        Long numberTag,
        String profileImage,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
