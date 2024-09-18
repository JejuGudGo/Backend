package com.gudgo.jeju.domain.post.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ChatRoomListResponse(
        Long chatRoomId,
        String title,
        List<String> profileImages,
        String recentMessage,
        LocalDateTime createdAt // 제일 최근 메세지의 생성일자
) {
}
