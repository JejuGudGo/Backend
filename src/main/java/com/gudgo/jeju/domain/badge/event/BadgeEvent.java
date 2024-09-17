package com.gudgo.jeju.domain.badge.event;

import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadgeEvent {
    private final Long userId;
    private final BadgeCode code;
}
