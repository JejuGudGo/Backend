package com.example.jejugudgo.global.api.tourapi.area.dto;

public record TourApiSpotResponse(
        String title,
        String contentId,
        String summary,
        String imageUrl,
        String address,
        double latitude,
        double longitude,
        String phone,
        String homepage,
        String fee,
        String openingHours,
        String eventStartDate,
        String eventEndDate,
        String eventContent,
        String eventPlace,
        String eventFee,
        String sponsor
) {
}
