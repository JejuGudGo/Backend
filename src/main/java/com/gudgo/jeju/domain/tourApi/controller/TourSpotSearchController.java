package com.gudgo.jeju.domain.tourApi.controller;

import com.gudgo.jeju.domain.planner.dto.request.spot.CurrentLocationDto;
import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.planner.component.TourApiSpotFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/courses/tour/")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TourSpotSearchController {

    private final TourApiSpotFinder tourApiSpotFinder;

    @GetMapping(value = "/spots")
    public ResponseEntity<?> searchTourApiSpot(CurrentLocationDto requestDto) {
        double latitude = requestDto.latitude();
        double longitude = requestDto.longitude();

        List<TourApiSpotResponseDto> tourSpots = tourApiSpotFinder.searchTourApiSpots(latitude, longitude);

        if (tourSpots.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tourSpots);
    }
}
