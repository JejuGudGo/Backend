package com.gudgo.jeju.domain.todo.dto.request;

import com.gudgo.jeju.domain.todo.entity.TodoType;

public record TodoCreateRequestDto(
        TodoType type,
        Long orderNumber,
        String content
) {
}
