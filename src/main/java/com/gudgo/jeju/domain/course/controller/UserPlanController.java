package com.gudgo.jeju.domain.course.controller;


import com.gudgo.jeju.domain.course.dto.request.PlanCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.PlanUpdateIsCompletedRequestDto;
import com.gudgo.jeju.domain.course.dto.request.PlanUpdateStartRequestDto;
import com.gudgo.jeju.domain.course.dto.response.PlanResponseDto;
import com.gudgo.jeju.domain.course.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserPlanController {
    private final PlanService planService;


   /* POST: 걷기 계획 생성
   *  POST: /api/v1/plan/user   */
    @PostMapping(value = "/plan")
    public ResponseEntity<?> plan(@Valid @RequestBody PlanCreateRequestDto planCreateRequestDto, HttpServletRequest request) {
        planService.newPlan(planCreateRequestDto, request);
        return ResponseEntity.ok().build();
    }

    /* GET: 걷기 계획 목록 조회
     * GET: /api/v1/plans/user   */

    // 유저 걷기 계획 목록 조회 (parameter : userid)
    @GetMapping(value = "/plans/user")
    public ResponseEntity<List<PlanResponseDto>> getPlanList(HttpServletRequest request) {
        return ResponseEntity.ok(planService.getPlanListByUser(request));
    }

    /* GET: CourseId로 걷기 계획 조회
     * GET: /api/v1/plan?courseId={courseId}   */
    @GetMapping(value = "/plan")
    public ResponseEntity<PlanResponseDto> getPlan(@RequestParam Long courseId) {
        return ResponseEntity.ok(planService.getPlanByCourseId(courseId));

    }

    /* PATCH : 걷기 계획 이벤트 시작일 수정
     * PATCH : /api/v1/plan/{courseId}/update-start  */
    // 유저 걷기 계획 이벤트 시작일 수정
    @PatchMapping(value = "/plan/{courseId}/update-start")
    public ResponseEntity<?> updatePlanStartAt(@PathVariable Long courseId, @Valid @RequestBody PlanUpdateStartRequestDto requestDto) {
        planService.updatePlanStartAt(courseId, requestDto);
        return ResponseEntity.ok().build();
    }


    /* PATCH : 걷기 계획 완료 여부 수정
    *  PATCH : /api/vi/plan/{courseId}/update-completed */
    @PatchMapping(value = "/plan/{courseId}/update-completed")
    public ResponseEntity<?> updatePlanIsCompleted(@PathVariable Long courseId, @Valid @RequestBody PlanUpdateIsCompletedRequestDto requestDto) {
        planService.updatePlanIsCompleted(courseId);
        return ResponseEntity.ok().build();
    }

    /* DELETE : 유저 걷기 계획 삭제
     * DELETE /api/v1/plan/{courseId}) */
    @DeleteMapping(value = "/plan/{courseId}")
    public ResponseEntity<?> deletePlan(@PathVariable Long courseId) {
        planService.deletePlan(courseId);
        return ResponseEntity.ok().build();
    }
}
