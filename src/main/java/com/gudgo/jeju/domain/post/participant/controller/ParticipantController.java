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

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping(value = "/{postId}/participants/join/{userId}")
    public ResponseEntity<?> requestJoin(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId,
            @RequestBody ParticipantJoinRequest request) {
        participantService.requestJoin(postId, userId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{postId}/participants/approve/{userId}")
    public ResponseEntity<ParticipantApproveResponse> approveUserOrNot(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId,
            @RequestParam("status") boolean status,
            ParticipantApproveResponse participantApproveResponse
    ) {
        participantService.approveUserOrNot(postId, userId, status);
        return ResponseEntity.ok(participantApproveResponse);
    }

    //    @PatchMapping(value = "/{postId}/participants/cancel/{userId}")
//    public ResponseEntity<?> requestCancel(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
//        participantService.requestCancel(postId, userId);
//        return ResponseEntity.ok().build();
//    }

}
