package com.example.jejugudgo.domain.event.entity;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum EventStatus {
    PROGRESS("진행중"),
    SCHEDULED("진행예정"),
    END("종료");

    private final String code;

    EventStatus(String code) {
        this.code = code;
    }

    public static EventStatus fromCode(String code) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.getCode().equals(code)) {
                return eventStatus;
            }
        }
        return null;
    }

    public static EventStatus getEventStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return EventStatus.SCHEDULED;
        } else if (today.isAfter(endDate)) {
            return EventStatus.END;
        } else if (today.isAfter(startDate) && (today.isBefore(endDate) || today.isEqual(endDate))) {
            return EventStatus.PROGRESS;
        }
        return EventStatus.END;
    }
}
