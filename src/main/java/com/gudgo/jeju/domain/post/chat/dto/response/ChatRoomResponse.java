package com.gudgo.jeju.domain.post.chat.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ChatRoomResponse(
        Long chatRoomId,
        String title,
        long participantCount,
        LocalDate createdAt, // 채팅방 개설일자 = planner 생성 일자
        List<ChattingUserResponse> responses,
        List<String> messages // 최근 메세지 100개
) { }
