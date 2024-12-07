package com.example.jejugudgo.domain.event.entity;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum EventStatus {
    PROGRESS("진행중"),
    SCHEDULED("진행예정"),
    END("종료");

    private final String status;

    EventStatus(String status) {
        this.status = status;
    }

    public static EventStatus fromInput(String input) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.getStatus().equals(input)) {
                return eventStatus;
            }
        }
        return null;
    }
}
