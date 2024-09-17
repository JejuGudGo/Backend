package com.gudgo.jeju.domain.planner.planner.controller;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.service.CourseService;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerCreateResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerDetailResponse;
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
//    private final PlannerQueryService plannerQueryService;
    private final PlannerService plannerService;
    private final CourseService courseService;
    private final SpotService spotService;
    private final PlannerQueryService plannerQueryService;

    //    @GetMapping("/planners/{plannerId}/info")
//    public Page<UserPlannerDetailResponse> getUserPlanners(Pageable pageable) {
//        return plannerQueryService.getUserPlanners(pageable);
//    }
//
//    @GetMapping("/planners/course/olle")
//    public Page<PlannerResponse> getOllePlanners(Pageable pageable) {
//        return plannerQueryService.getOllePlanners(pageable);
//    }
//
//    @GetMapping("/planners/course/all")
//    public Page<PlannerResponse> getAllPlanners(Pageable pageable) {
//        return plannerQueryService.getAllPlanners(pageable);
//    }
//
//    @GetMapping("/planners/course/label")
//    public Page<PlannerResponse> getPlannersByLabel(Pageable pageable, LabelRequestDto requestDto) {
//        return plannerQueryService.getPlannersByLabel(pageable, requestDto);
//    }
//
//
//    @GetMapping("/planners/users/{plannerId}")
//    public ResponseEntity<PlannerResponse> getUserCourse(@PathVariable("plannerId") Long plannerId) {
//        return ResponseEntity.ok(plannerQueryService.getUserPlanner(plannerId));
//    }
//
//    @GetMapping("/planners/olle/{plannerId}")
//    public ResponseEntity<PlannerResponse> getOlleCourse(@PathVariable("plannerId") Long plannerId) {
//        return ResponseEntity.ok(plannerQueryService.getOllePlanner(plannerId));
//    }
//
//
//    @GetMapping("/users/{userId}/planners")
//    public Page<PlannerResponse> getMyPlanners(
//            @PathVariable("userId") Long userId,
//            @RequestParam("status") String status,
//            Pageable pageable
//    ) {
//        if (status.equals("all")) {
//            return plannerQueryService.getMyPlanners(userId, pageable);
//
//        } else if (status.equals("false")) {
//            return plannerQueryService.getMyUncompletedPlanners(userId, pageable);
//        }
//
//        return null;
//    }
//
//
    @GetMapping("/planners/{plannerId}")
    public ResponseEntity<PlannerDetailResponse> getPlanner(@PathVariable("plannerId") Long plannerId) {
        PlannerDetailResponse response = plannerQueryService.getUserPlannerDetail(plannerId);

        return ResponseEntity.ok(response);
    }

    // 유저 직접 생성
    @PostMapping("/users/{userId}/planners")
    public ResponseEntity<PlannerCreateResponse> create(@PathVariable("userId") Long userId, @Valid @RequestBody PlannerCreateRequestDto requestDto) {
        Course course = courseService.createCourse(requestDto.courseCreateRequestDto());
        List<SpotCreateResponse> spots = spotService.createUserSpot(course, requestDto.spotCreateRequestDto());
        PlannerCreateResponse response = plannerService.create(userId, requestDto, course, spots);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/planners/{plannerId}")
    public ResponseEntity<?> delete(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        plannerService.delete(plannerId);

        return ResponseEntity.ok().build();
    }
}