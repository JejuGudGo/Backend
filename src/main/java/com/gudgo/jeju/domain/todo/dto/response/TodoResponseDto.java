package com.gudgo.jeju.domain.todo.dto.response;

public record TodoResponseDto(
        Long id,
        String content,
        boolean isFinished
) {
}
