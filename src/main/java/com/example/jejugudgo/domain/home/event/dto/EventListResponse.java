package com.example.jejugudgo.domain.home.event.dto;

public record EventListResponse(
        Long eventId,
        String title,
        String status,
        String thumbnail,
        String homepage
) {
}
