package com.gudgo.jeju.domain.todo.dto.response;

import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;

public record TodoResponseDto(
        Long id,
        String content,
        boolean isFinished
) {
}
