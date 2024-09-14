package com.gudgo.jeju.domain.tourApi.controller;

import com.gudgo.jeju.domain.planner.spot.dto.request.CategoryRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.CurrentLocationDto;
import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.tourApi.component.TourApiSpotFinder;
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
    public ResponseEntity<?> searchTourApiSpot(CurrentLocationpDto currentLocationDto, CategoryRequestDto categoryRequestDto) {
        double latitude = currentLocationDto.latitude();
        double longitude = currentLocationDto.longitude();
        String categoryId = categoryRequestDto.tourApiCategory1Id();

        List<TourApiSpotResponseDto> tourSpots = tourApiSpotFinder.searchTourApiSpots(latitude, longitude, categoryId);

        return ResponseEntity.ok(tourSpots);
    }
}
