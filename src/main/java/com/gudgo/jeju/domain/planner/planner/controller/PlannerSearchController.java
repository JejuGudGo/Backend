package com.gudgo.jeju.domain.planner.planner.controller;

import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerSearchResponse;
import com.gudgo.jeju.domain.planner.planner.query.PlannerSearchQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/planners")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PlannerSearchController {
    private final PlannerSearchQueryService plannerSearchQueryService;

    @GetMapping("")
    public Page<PlannerSearchResponse> getPlanners(@RequestParam("title") String title, Pageable pageable) {
        return plannerSearchQueryService.searchPlannersByTitle(pageable, title);
    }
}
