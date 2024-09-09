package com.gudgo.jeju.domain.post.chat.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ChatRoomResponse(
        Long chatRoomId,
        String title,
//        LocalDate createdAt,
        List<String> profileImages
) {
}
