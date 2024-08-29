package com.gudgo.jeju.domain.post.participant.controller;

import com.gudgo.jeju.domain.post.participant.dto.response.ParticipantApproveResponse;
import com.gudgo.jeju.domain.post.participant.dto.request.ParticipantJoinRequest;
import com.gudgo.jeju.domain.post.participant.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.post.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/planners")
@RequiredArgsConstructor
@Slf4j
@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping(value = "/{plannerId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable("plannerId") Long plannerId, @RequestParam("status") boolean status) {
        return ResponseEntity.ok(participantService.getParticipants(plannerId, status));
    }

    @PostMapping(value = "/{plannerId}/participants/join/{userId}")
    public ResponseEntity<?> requestJoin(
            @PathVariable("plannerId") Long courseId,
            @PathVariable("userId") Long userId,
            @RequestBody ParticipantJoinRequest request) {
        participantService.requestJoin(courseId, userId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{plannerId}/participants/cancel/{userId}")
    public ResponseEntity<?> requestCancel(@PathVariable("plannerId") Long plannerId, @PathVariable("userId") Long userId) {
        participantService.requestCancel(plannerId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{plannerId}/participants/approve/{userId}")
    public ResponseEntity<ParticipantApproveResponse> approveUserOrNot(
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("userId") Long userId,
            @RequestParam("status") boolean status,
            ParticipantApproveResponse participantApproveResponse
    ) {
        participantService.approveUserOrNot(plannerId, userId, status);
        return ResponseEntity.ok(participantApproveResponse);
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
