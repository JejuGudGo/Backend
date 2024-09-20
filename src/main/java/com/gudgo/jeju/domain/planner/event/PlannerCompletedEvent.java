package com.gudgo.jeju.domain.planner.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlannerCompletedEvent {
    private final Long plannerId;
}
