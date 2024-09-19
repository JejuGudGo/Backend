package com.gudgo.jeju.domain.post.chat.dto.response;

public record ChattingUserResponse(
        Long userId,
        String profileImg,
        boolean isHost,
        String userNickname
) {
}
