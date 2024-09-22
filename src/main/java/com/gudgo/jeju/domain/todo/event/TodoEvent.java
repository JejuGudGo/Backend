package com.gudgo.jeju.domain.todo.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoEvent {
    private final Long userId;
}
