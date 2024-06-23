package com.gudgo.jeju.domain.course.service;

import com.gudgo.jeju.domain.course.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.Participant;
import com.gudgo.jeju.domain.course.query.ParticipantQueryService;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import com.gudgo.jeju.domain.course.repository.ParticipantRepository;
import com.gudgo.jeju.domain.course.validation.ParticipantValidator;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
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

    public List<ParticipantResponse> getParticipants(Long courseId, boolean status) {
        if (status) {
            return getApprovedParticipants(courseId);
        } else {
            return getNotApprovedParticipants(courseId);
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
    public void requestJoin(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        participantValidator.validateParticipantNumber(course.getPostId(), courseId);

        Optional<Participant> optionalParticipant =
                participantRepository.findByParticipantUserIdAndCourseId(userId, courseId);

        // 신청 이력이 있는 경우
        Participant participant = optionalParticipant
                .orElseGet(() -> Participant.builder()
                        .participantUserId(userId)
                        .course(course)
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
    public void requestCancel(Long courseId, Long userId) {
        Participant participant = participantRepository.findByParticipantUserIdAndCourseId(userId, courseId)
                .orElseThrow(EntityNotFoundException::new);

        participant = participant.withApplied(false);

        participantRepository.save(participant);
    }

    public void approveUserOrNot(Long courseId, Long userId, boolean status) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        Posts post = postsRepository.findById(course.getPostId())
                .orElseThrow(EntityNotFoundException::new);

        Participant participant = participantRepository.findByParticipantUserIdAndCourseId(userId, courseId)
                .orElseThrow(EntityNotFoundException::new);

        if (status) {
            approveUser(post, participant, courseId, userId);

        } else {
            notApproveUser(post, participant, courseId, userId);
        }
    }

    private void approveUser(Posts post, Participant participant, Long courseId, Long userId) {
        participantValidator.validateParticipantNumber(post.getId(), courseId);

        participant = participant.withApproved(true);
        participantRepository.save(participant);

        if (post.getCompanionsNum().equals(participantQueryService.countCourseParticipant(courseId))) {
            post = post.withIsFinished(true);
            postsRepository.save(post);
        }
    }

    private void notApproveUser(Posts post, Participant participant, Long courseId, Long userId) {
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
