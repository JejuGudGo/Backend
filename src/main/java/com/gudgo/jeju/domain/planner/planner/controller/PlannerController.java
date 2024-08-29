package com.gudgo.jeju.domain.planner.planner.controller;

import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerResponse;
import com.gudgo.jeju.domain.planner.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.planner.service.PlannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PlannerController {
    private final PlannerQueryService plannerQueryService;
    private final PlannerService plannerService;

    @GetMapping("/planners/course/user")
    public Page<PlannerResponse> getPlanners(Pageable pageable) {
        return plannerQueryService.getUserPlanners(pageable);
    }

    @GetMapping("/users/{userId}/planners")
    public Page<PlannerResponse> getMyPlanners(
            @PathVariable("userId") Long userId,
            @RequestParam("status") String status,
            Pageable pageable
    ) {
        if (status.equals("all")) {
            return plannerQueryService.getMyPlanners(userId, pageable);

        } else if (status.equals("false")) {
            return plannerQueryService.getMyUncompletedPlanners(userId, pageable);
        }

        return null;
    }

    @GetMapping("/users/{userId}/planners/{plannerId}")
    public PlannerResponse getPlanner(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        return plannerQueryService.getPlanners(userId, plannerId);
    }

    // 유저 직접 생성
    @PostMapping("/users/{userId}/planners")
    public ResponseEntity<?> create(@PathVariable("userId") Long userId, @Valid @RequestBody PlannerCreateRequestDto requestDto) {
        plannerService.create(userId, requestDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userId}/planners/{plannerId}")
    public ResponseEntity<?> update(
            @PathVariable("userId") Long userId,
            @PathVariable("plannerId") Long plannerId,
            @RequestBody PlannerUpdateRequestDto requestDto
    ) {
        plannerService.update(plannerId, requestDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/planners/{plannerId}")
    public ResponseEntity<?> delete(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        plannerService.delete(plannerId);

        return ResponseEntity.ok().build();
    }
}