package com.gudgo.jeju.domain.planner.service;

import com.gudgo.jeju.domain.planner.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.Participant;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.validation.ParticipantValidator;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void requestJoin(Long plannerId, Long userId) {

        Posts post = postsRepository.findByPlannerId(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        participantValidator.validateParticipantNumber(post.getId(), plannerId);

        Optional<Participant> optionalParticipant =
                participantRepository.findByUserIdAndPlannerId(userId, plannerId);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        // 신청 이력이 있는 경우
        Participant participant = optionalParticipant
                .orElseGet(() -> Participant.builder()
                        .user(user)
                        .planner(planner)
                        .count(0L) // 처음 생성할 때 count를 0으로 설정
                        .isApplied(false) // 처음 생성할 때 isApplied를 false로 설정
                        .build());

        if (participant.getCount() >= 3) {  // 신청 이력이 3회 이상일 경우
            throw new IllegalStateException("request count를 초과하였습니다.");

        } else {
            participant = participant.withCountAndApplied(true);

            participantRepository.save(participant);
        }
    }


    @Transactional
    public void requestCancel(Long plannerId, Long userId) {
        Participant participant = participantRepository.findByUserIdAndPlannerId(userId, plannerId)
                .orElseThrow(EntityNotFoundException::new);

        participant = participant.withApplied(false);

        participantRepository.save(participant);
    }

    public void approveUserOrNot(Long plannerId, Long userId, boolean status) {

        Posts post = postsRepository.findByPlannerId(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        Participant participant = participantRepository.findByUserIdAndPlannerId(userId, plannerId)
                .orElseThrow(EntityNotFoundException::new);

        if (status) {
            approveUser(post, participant, plannerId);

        } else {
            notApproveUser(post, participant, plannerId);
        }
    }

    private void approveUser(Posts post, Participant participant, Long courseId) {
        participantValidator.validateParticipantNumber(post.getId(), courseId);

        participant = participant.withApproved(true);
        participantRepository.save(participant);

        if (post.getCompanionsNum().equals(participantQueryService.countCourseParticipant(courseId))) {
            post = post.withIsFinished(true);
            postsRepository.save(post);
        }
    }

    private void notApproveUser(Posts post, Participant participant, Long courseId) {
        participant.withApproved(false);
        participantRepository.save(participant);

        // 특정 user의 승인을 취소했을 때, 모집 마감상태라면, isFinished : false로 변경
        if (participantQueryService.countCourseParticipant(courseId) < post.getCompanionsNum() && post.isFinished()) {
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
