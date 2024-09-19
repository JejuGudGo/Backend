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
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("query") String title,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("category3") List<String> category3,
            Pageable pageable
    ) {
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


