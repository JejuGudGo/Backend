package com.gudgo.jeju.global.data.tourAPI.spot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TourApiSpotDto(
        Long id,
        String title,
        String address,
        String content,
        String pageUrl,
        String info,
        String closeDay,
        String organizerInfo,
        String organizeNumber,
        String eventStartDate,
        String eventEndDate,
        String fee,
        String time,
        String park,
        String rentStroller,
        String availablePet,
        String eventContent,
        String eventFee,
        String eventPlace,
        String guideService,
        String toilet,
        String reserveInfo
){}
