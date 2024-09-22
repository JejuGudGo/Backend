package com.gudgo.jeju.domain.planner.planner.controller;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.service.CourseService;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerCreateResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerDetailResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerUserResponse;
import com.gudgo.jeju.domain.planner.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.planner.service.PlannerService;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotCreateResponse;
import com.gudgo.jeju.domain.planner.spot.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PlannerController {
    private final PlannerService plannerService;
    private final CourseService courseService;
    private final SpotService spotService;
    private final PlannerQueryService plannerQueryService;

    // 특정 유저 생성 코스
    @GetMapping("/users/{userId}/planners/created-planners")
    public ResponseEntity<List<PlannerListResponse>> getUserCreatedPlanners(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(plannerQueryService.getUserCreatedPlanners(userId));
    }

    // 특정 유저 이용 코스
    @GetMapping("/users/{userId}/planners/completed-planners")
    public ResponseEntity<List<PlannerListResponse>> getUserCompletedPlanners(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(plannerQueryService.getUserCompletedPlanners(userId));
    }

    @GetMapping("/users/{userId}/planners/{plannerId}")
    public ResponseEntity<PlannerDetailResponse> getPlanner(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        PlannerDetailResponse response = plannerQueryService.getUserPlannerDetail(plannerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/planners/top10")
    public ResponseEntity<List<PlannerListResponse>> getTopRatedPlanners(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(plannerQueryService.getTopRatedPlanners());
    }

    // 마이걷고 탭 조회
    @GetMapping("/users/{userId}/planners/mygudgo")
    public ResponseEntity<PlannerUserResponse> getPlannerUserInfo(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(plannerQueryService.getPlannerUserInfo(userId));
    }

    // 유저 직접 생성
    @PostMapping("/users/{userId}/planners")
    public ResponseEntity<PlannerCreateResponse> create(@PathVariable("userId") Long userId, @Valid @RequestBody PlannerCreateRequestDto requestDto) {
        Course course = courseService.createCourse(requestDto.courseCreateRequestDto());
        List<SpotCreateResponse> spots = spotService.createUserSpot(course, requestDto.spotCreateRequestDto());
        PlannerCreateResponse response = plannerService.create(userId, requestDto, course, spots);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/planners/{plannerId}")
    public ResponseEntity<?> delete(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        plannerService.delete(plannerId);

        return ResponseEntity.ok().build();
    }
}