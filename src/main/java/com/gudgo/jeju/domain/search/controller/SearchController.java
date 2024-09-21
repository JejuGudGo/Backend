package com.gudgo.jeju.domain.search.controller;

import com.gudgo.jeju.domain.search.dto.response.SearchListResponse;
import com.gudgo.jeju.domain.search.query.SearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value="/api/v1/search")
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchQueryService searchQueryService;

    @GetMapping("")
    public Page<SearchListResponse> search(
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "query", required = false) String title,
            @RequestParam(value = "category1") String category1,
            @RequestParam(value = "category2", required = false) List<String> category2,
            @RequestParam(value = "category3", required = false) List<String> category3,
            Pageable pageable
    ) {
        if (category3 == null) {
            category3 = List.of();
        }

        return searchQueryService.search(title, category1, category2, category3, latitude, longitude, pageable);
    }

    @GetMapping(value = "/planners/{plannerId}")
    public ResponseEntity<SearchListResponse> getUserPlanner(@PathVariable Long plannerId) {
        SearchListResponse response = searchQueryService.getPlanner(plannerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/trails/{trailId}")
    public ResponseEntity<SearchListResponse> getTrail(@PathVariable Long trailId) {
        SearchListResponse response = searchQueryService.getTrail(trailId);

        return ResponseEntity.ok(response);
    }
}


