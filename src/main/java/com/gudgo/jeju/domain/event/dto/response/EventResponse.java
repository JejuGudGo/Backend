package com.gudgo.jeju.domain.event.dto.response;

import com.gudgo.jeju.domain.event.entity.EventType;

public record EventResponse(
        Long eventId,
        String title,
        String organization,
        String period,
        String imageUrl,
        String informationUrl,
        EventType type
) {
}
