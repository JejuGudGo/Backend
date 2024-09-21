package com.gudgo.jeju.domain.event.dto.response;

import com.gudgo.jeju.domain.event.entity.EventType;

import java.time.LocalDate;

public record EventResponse(
        Long eventId,
        String title,
        String organization,
        LocalDate startDate,
        LocalDate finishDate,
        String imageUrl,
        String informationUrl,
        EventType type
) {
}
