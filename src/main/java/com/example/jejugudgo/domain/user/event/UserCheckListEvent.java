package com.example.jejugudgo.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCheckListEvent {
    private final Long userId;
}