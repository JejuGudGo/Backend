package com.gudgo.jeju.domain.post.chat.dto.request;

public record NoticeUpdateRequest (
        Long chatRoomId,
        String content
) { }
