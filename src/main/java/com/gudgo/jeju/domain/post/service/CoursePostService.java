package com.gudgo.jeju.domain.post.service;

import com.gudgo.jeju.domain.course.dto.request.participant.ParticipantJoinRequest;
import com.gudgo.jeju.domain.course.dto.response.ParticipantResponse;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.Participant;
import com.gudgo.jeju.domain.course.query.ParticipantQueryService;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import com.gudgo.jeju.domain.course.repository.ParticipantRepository;
import com.gudgo.jeju.domain.post.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.entity.PostType;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoursePostService {
    private final PostsRepository postsRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantQueryService participantQueryService;

    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;

    private final ValidationUtil validationUtil;


//    public CoursePostResponse get(Long postId) {
//        Posts posts = postsRepository.findById(postId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        LocalDate courseStartDate = posts.getCourse().getStartAt();
//
//        if (courseStartDate.isAfter(LocalDate.now()) && !posts.isFinished()) {
//
//            CoursePostResponse coursePostResponse = getResponse(posts);
//
//            return coursePostResponse;
//        }
//
//        return null;
//    }

    public CoursePostResponse getCoursePost(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("posts not found id=" + postId));

        Long courseId = findPostById(postId).getCourse().getId();
        Long currentParticipantNum = participantRepository.countByCourseIdAndApprovedTrue(courseId);

        return new CoursePostResponse(
                posts.getId(),
                posts.getUser().getId(),
                posts.getUser().getNickname(),
//                posts.getUser().getProfile().getProfileImageUrl(),
                posts.getUser().getNumberTag(),
                posts.getCourse().getId(),
                posts.getTitle(),
                posts.getCompanionsNum(),
                currentParticipantNum,
                posts.getContent()

        );


    }

    @Transactional
    public CoursePostResponse create(CoursePostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(EntityNotFoundException::new);

        Posts posts = Posts.builder()
                .user(user)
                .course(course)
                .postType(PostType.COURSE)
                .title(request.title())
                .content(request.content())
                .companionsNum(request.companionsNum())
                .createdAt(LocalDate.now())
                .isFinished(false)
                .isDeleted(false)
                .build();

        postsRepository.save(posts);
        return getResponse(posts);
    }

    @Transactional
    public CoursePostResponse update(Long postId, CoursePostUpdateRequest request) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Posts updatedPosts = null;
        if (validationUtil.validateStringValue(request.title())) {
            updatedPosts = posts.withTitle(request.title());

        }

        if (validationUtil.validateStringValue(request.content())) {
            updatedPosts = posts.withContent(request.content());

        }

        if (validationUtil.validateLongValue(request.courseId())) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(EntityNotFoundException::new);

            updatedPosts = posts.withCourse(course);

        }

        if (validationUtil.validateLongValue(request.participantNum())) {
            updatedPosts = posts.withCompanionsNum(request.participantNum());
        }

        if (request.isFinished() != posts.isFinished()) {
            updatedPosts = posts.withIsFinished(request.isFinished());
        }

        return getResponse(postsRepository.save(updatedPosts));
    }

    @Transactional
    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Posts updatedPosts = posts.withIsFinishedAndIsDeleted(true, true);
        postsRepository.save(updatedPosts);
    }

    @Transactional
    public void requestJoin(Long postId, HttpServletRequest request) {
        Long userId = getUser(request).getId();
        Long courseId = findPostById(postId).getCourse().getId();

        Optional<Participant> participantOptional =
                participantRepository.findByParticipantUserIdAndCourseId(userId, courseId);

        // 신청 이력이 있는 경우
        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();

            if (participant.getCount() >= 3) {  // 신청 이력이 3회 이상일 경우
                throw new IllegalStateException("request count를 초과하였습니다.");
            } else {
                Participant updatedParticipant = participant.withCountAndApplied(true);
                participantRepository.save(updatedParticipant);
            }
            return;
        }

        // 신청 이력이 없는 경우
        Participant newApplication = Participant.builder()
                .participantUserId(userId)
                .course(findPostById(postId).getCourse())
                .count(1L)
                .isApplied(true)
                .build();
        participantRepository.save(newApplication);
    }

    @Transactional
    public void requestCancel(Long postId, HttpServletRequest request) {
        Long userId = getUser(request).getId();
        Long courseId = findPostById(postId).getCourse().getId();

        Participant participant = participantRepository.findByParticipantUserIdAndCourseId(userId, courseId)
                .orElseThrow(EntityNotFoundException::new);
        Participant updatedParticipant = participant.withApplied(false);
        participantRepository.save(updatedParticipant);
    }


    public List<ParticipantResponse> getParticipants(Long postId) {
        Long courseId = findPostById(postId).getCourse().getId();
        List<Participant> participants = participantRepository.findByCourseIdAndApprovedFalseAndIsAppliedTrue(courseId);
        return mapToParticipantResponseList(participants);
    }

    public List<ParticipantResponse> getApprovedParticipants(Long postId) {

        Long courseId = findPostById(postId).getCourse().getId();
        List<Participant> participants = participantRepository.findByCourseIdAndApprovedTrue(courseId);
        return mapToParticipantResponseList(participants);
    }

    public void approveUser(Long postId, Long userId) {

        Posts post = findPostById(postId);
        Long courseId = findPostById(postId).getCourse().getId();

        // 이미 동행인원수가 다 채워졌을 때
        if (participantRepository.findByCourseIdAndApprovedTrue(courseId).equals(post.getCompanionsNum())) {
            // 에러 발생
            throw new IllegalStateException("The number of companions is already full for courseId=" + courseId);
        }

        Participant participant = participantRepository.findByParticipantUserIdAndCourseId(userId, courseId)
                .orElseThrow(EntityNotFoundException::new);
        Participant updatedParticipant = participant.withApproved(true);
        participantRepository.save(updatedParticipant);

        if (participantRepository.countByCourseIdAndApprovedTrue(courseId).equals(post.getCompanionsNum())) {
            Posts updatedPost = post.withIsFinished(true);
            postsRepository.save(updatedPost);
        }
    }

    public void notApproveUser(Long postId, Long userId) {
        Long courseId = findPostById(postId).getCourse().getId();
        Participant participant = findParticipantByUserIdAndCourseId(userId, courseId);
        Participant updatedParticipant = participant.withApproved(false);
        participantRepository.save(updatedParticipant);

        // 특정 user의 승인을 취소했을 때, 모집 마감상태라면, isFinished : false로 변경
        Posts post = findPostById(postId);
        if (participantRepository.countByCourseIdAndApprovedTrue(courseId) < post.getCompanionsNum() && post.isFinished()) {
            Posts updatedPost = post.withIsFinished(false);
            postsRepository.save(updatedPost);
        }
    }

    public void finishRecruit(Long postId) {
        Posts post = findPostById(postId);
        Posts updatedPost = post.withIsFinished(true);
        postsRepository.save(updatedPost);
    }


    private CoursePostResponse getResponse(Posts posts) {
        CoursePostResponse coursePostResponse = new CoursePostResponse(
                posts.getId(),
                posts.getUser().getId(),
                posts.getUser().getNickname(),
//                posts.getUser().getProfile().getProfileImageUrl(),
                posts.getUser().getNumberTag(),
                posts.getCourse().getId(),
                posts.getTitle(),
                posts.getCompanionsNum(),
                participantQueryService.countCourseParticipant(posts.getCourse().getId()),
                posts.getContent()
        );

        return coursePostResponse;
    }

    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        // userid로 User 객체 조회
        return userRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
    }


    private Posts findPostById(Long postId) {
        return postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found id=" + postId));
    }

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found id=" + courseId));
    }

    private Participant findParticipantByUserIdAndCourseId(Long userId, Long courseId) {
        return participantRepository.findByParticipantUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found for userId=" + userId + " and courseId=" + courseId));
    }

    private List<ParticipantResponse> mapToParticipantResponseList(List<Participant> participants) {
        return participants.stream()
                .map(participant -> new ParticipantResponse(
                        participant.getId(),
                        participant.getCourse().getId(),
                        participant.getParticipantUserId(),
                        participant.isApproved(),
                        participant.isDeleted(),
                        participant.getCount(),
                        participant.isApplied()
                )).collect(Collectors.toList());
    }
}
