package com.example.jejugudgo.domain.event.dto;

public record EventListResponse(
        Long eventId,
        String title,
        String status,
        String thumbnail,
        String link
) {
}
