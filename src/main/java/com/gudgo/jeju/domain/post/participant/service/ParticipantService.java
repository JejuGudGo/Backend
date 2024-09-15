package com.gudgo.jeju.domain.post.participant.service;

import com.gudgo.jeju.domain.post.participant.dto.request.ParticipantJoinRequest;
import com.gudgo.jeju.domain.post.participant.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.post.participant.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.post.participant.validation.ParticipantValidator;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final CourseRepository courseRepository;
    private final PostsRepository postsRepository;
    private final ParticipantQueryService participantQueryService;
    private final ParticipantValidator participantValidator;
    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;

    public List<ParticipantResponse> getParticipants(Long plannerId, boolean status) {
        if (status) {
            return getApprovedParticipants(plannerId);
        } else {
            return getNotApprovedParticipants(plannerId);
        }
    }

    // 승낙 된 참가자 리스트
    private List<ParticipantResponse> getApprovedParticipants(Long courseId) {
        return participantQueryService.getApprovedParticipants(courseId);
    }

    // 승낙 기다리는 참가자 리스트
    private List<ParticipantResponse> getNotApprovedParticipants(Long courseId) {
        return participantQueryService.getUnApprovedParticipants(courseId);
    }

    @Transactional
    public void requestJoin(Long postId, Long userId, ParticipantJoinRequest request) {

        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Long plannerId = post.getPlanner().getId();

        participantValidator.validateParticipantNumber(post.getId(), plannerId);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Participant optionalParticipant =
                participantRepository.findByUserIdAndPlannerId(userId, plannerId);

        if (optionalParticipant != null) {
            if (optionalParticipant.getCount() >= 3) { // 신청 이력이 3회 이상일 경우
                throw new IllegalStateException("request count 를 초과하였습니다.");

            } else {
                optionalParticipant = optionalParticipant.withCountAndIsAppliedAndAppliedAt(true, LocalDate.now());
                participantRepository.save(optionalParticipant);
            }

        } else {
            Participant participant = Participant.builder()
                    .user(user)
                    .planner(post.getPlanner())
                    .isHost(false)
                    .approved(false)
                    .isApplied(true)
                    .appliedAt(LocalDate.now())
                    .isDeleted(false)
                    .isBlocked(false)
                    .content(request.content())
                    .count(0L)
                    .build();
            participantRepository.save(participant);
        }
    }


    @Transactional
    public void requestCancel(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Participant participant = participantRepository.findByUserIdAndPlannerId(userId, post.getPlanner().getId());

        participant = participant.withApplied(false);

        participantRepository.save(participant);
    }

    public void approveUserOrNot(Long postId, Long userId, boolean status) {

        Posts post = postsRepository.findByPlannerId(postId)
                .orElseThrow(EntityNotFoundException::new);

        Long plannerId = post.getPlanner().getId();

        Participant participant = participantRepository.findByUserIdAndPlannerId(userId, plannerId);

        if (status) {
            approveUser(post, participant, plannerId);

        } else {
            notApproveUser(post, participant, plannerId);
        }
    }

    private void approveUser(Posts post, Participant participant, Long plannerId) {
        participantValidator.validateParticipantNumber(post.getId(), plannerId);

        participant = participant.withApprovedAndApprovedAt(true, LocalDate.now());
        participantRepository.save(participant);

        if (post.getParticipantNum().equals(participantQueryService.countCourseParticipant(plannerId))) {
            post = post.withIsFinished(true);
            postsRepository.save(post);
        }
    }

    private void notApproveUser(Posts post, Participant participant, Long plannerId) {
        participant.withApprovedAndApprovedAt(false, LocalDate.now());
        participantRepository.save(participant);

        // 특정 user의 승인을 취소했을 때, 모집 마감상태라면, isFinished : false로 변경
        if (participantQueryService.countCourseParticipant(plannerId) < post.getParticipantNum() && post.isFinished()) {
            post = post.withIsFinished(false);
            postsRepository.save(post);
        }
    }

    //    public List<ParticipantResponse> getApprovedParticipants(Long postId) {
//
//        Long courseId = findPostById(postId).getCourse().getId();
//        List<Participant> participants = participantRepository.findByCourseIdAndApprovedTrue(courseId);
//        return mapToParticipantResponseList(participants);
//    }

}
