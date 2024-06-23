package com.gudgo.jeju.domain.course.controller;

import com.gudgo.jeju.domain.course.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.course.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/Courses")
@RequiredArgsConstructor
@Slf4j
@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping(value = "/{courseId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable("courseId") Long courseId, @RequestParam("status") boolean status) {
        return ResponseEntity.ok(participantService.getParticipants(courseId, status));
    }

    @PostMapping(value = "/{courseId}/participants/join/{userId}")
    public ResponseEntity<?> requestJoin(@PathVariable("courseId") Long courseId, @PathVariable("userId") Long userId) {
        participantService.requestJoin(courseId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{courseId}/participants/cancel/{userId}")
    public ResponseEntity<?> requestCancel(@PathVariable("courseId") Long courseId, @PathVariable("userId") Long userId) {
        participantService.requestCancel(courseId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{courseId}/participants/approve/{userId}/{status}")
    public ResponseEntity<?> approveUserOrNot(
            @PathVariable("courseId") Long courseId,
            @PathVariable("userId") Long userId,
            @PathVariable("status") boolean status
    ) {
        participantService.approveUserOrNot(courseId, userId, status);
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value="/{userId}/Courses/{courseId}/participants/approved")
//    public ResponseEntity<List<ParticipantResponse>> getApprovedParticipants(@PathVariable("postId") Long courseId) {
//        return ResponseEntity.ok(participantService.getApprovedParticipants(courseId));
//
//    }

//    @PatchMapping(value = "/{postId}/{userId}/participants/not-approve")
//    public ResponseEntity<?> notApproveUser(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
//        participantService.notApproveUser(postId, userId);
//        return ResponseEntity.ok().build();
//    }
}
