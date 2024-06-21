package com.gudgo.jeju.domain.course.controller;

import com.gudgo.jeju.domain.course.dto.request.UserLocationRequestDto;
import com.gudgo.jeju.domain.course.dto.response.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.course.service.TourApiSpotService;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TourSpotSearchController {

    private final TourApiSpotService tourApiSpotService;

    @GetMapping(value = "/tour-spots")
    public ResponseEntity<?> searchTourApiSpot(UserLocationRequestDto requestDto) {
        double latitude = requestDto.latitude();
        double longitude = requestDto.longitude();

        List<TourApiSpotResponseDto> tourSpots = tourApiSpotService.searchTourApiSpots(latitude, longitude);

        if (tourSpots.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tourSpots);
    }
}
