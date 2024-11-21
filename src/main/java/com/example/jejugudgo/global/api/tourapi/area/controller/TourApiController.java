package com.example.jejugudgo.global.api.tourapi.area.controller;

import com.example.jejugudgo.global.api.tourapi.area.dto.TourApiSpotListResponse;
import com.example.jejugudgo.global.api.tourapi.area.dto.TourApiSpotResponse;
import com.example.jejugudgo.global.api.tourapi.area.query.TourApiSpotQueryService;
import com.example.jejugudgo.global.api.tourapi.area.service.TourApiSpotService;
import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import com.example.jejugudgo.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search/spots/tour-api")
@RequiredArgsConstructor
public class TourApiController {
    private final ApiResponseUtil apiResponseUtil;
    private final TourApiSpotQueryService tourApiSpotQueryService;
    private final TourApiSpotService tourApiSpotService;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getTourApiSpots(
            @RequestParam("type") String type,
            Pageable pageable
    ) {
        Pageable page = PagingUtil.createPageable(pageable.getPageNumber(), pageable.getPageSize());
        List<TourApiSpotListResponse> spotListPage = tourApiSpotQueryService
                .getAllTourApiSpotsByContentType(ContentType.fromTitle(type), page);
        return ResponseEntity.ok(apiResponseUtil.success(spotListPage, "spots"));
    }

    @GetMapping("/{tourApiSpotId}")
    public ResponseEntity<CommonApiResponse> getTourApiSpots(@PathVariable("tourApiSpotId") Long tourApiSpotId) throws IOException {
        TourApiSpotResponse response = tourApiSpotService.getSpotDetail(tourApiSpotId);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}