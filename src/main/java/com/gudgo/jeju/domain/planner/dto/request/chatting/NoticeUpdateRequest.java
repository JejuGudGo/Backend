package com.gudgo.jeju.domain.planner.dto.request.chatting;

public record NoticeUpdateRequest (
        Long chatRoomId,
        String content
) { }
