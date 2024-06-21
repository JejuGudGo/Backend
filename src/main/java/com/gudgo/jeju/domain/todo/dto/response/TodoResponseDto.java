package com.gudgo.jeju.domain.todo.dto.response;

import com.gudgo.jeju.domain.todo.entity.TodoType;

public record TodoResponseDto(
        Long id,
        TodoType type,
        Long orderNumber,
        String content,
        boolean isFinished
) {
}
