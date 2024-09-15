package com.gudgo.jeju.domain.tourApi.controller;

import com.gudgo.jeju.domain.planner.spot.dto.request.CategoryRequestDto;
import com.gudgo.jeju.domain.tourApi.dto.SearchTourApiSpotRequest;
import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.tourApi.component.TourApiSpotFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> searchTourApiSpot(@RequestBody SearchTourApiSpotRequest request) {
        List<TourApiSpotResponseDto> tourSpots = tourApiSpotFinder.searchTourApiSpots(request.latitude(), request.longitude(), request.contentTypeId());
        return ResponseEntity.ok(tourSpots);
    }
}
