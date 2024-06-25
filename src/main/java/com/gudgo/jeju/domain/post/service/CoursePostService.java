package com.gudgo.jeju.domain.post.service;

import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.CourseType;
import com.gudgo.jeju.domain.planner.entity.Participant;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.query.ParticipantQueryService;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.post.dto.request.CoursePostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CoursePostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.entity.PostType;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CoursePostService {
    private final PostsRepository postsRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ParticipantQueryService participantQueryService;
    private final PlannerRepository plannerRepository;
    private final ParticipantRepository participantRepository;

    private final ValidationUtil validationUtil;


    public CoursePostResponse getCoursePost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("posts not found id=" + postId));



        Long currentParticipantNum = participantQueryService.countCourseParticipant(post.getPlanner().getId());

        return new CoursePostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getUser().getProfile().getProfileImageUrl(),
                post.getUser().getNumberTag(),
                post.getTitle(),
                post.getCompanionsNum(),
                currentParticipantNum,
                post.getContent()
        );
    }

    @Transactional
    public CoursePostResponse create(CoursePostCreateRequest request) {

        /* 1. Course 테이블에 카피 진행 */
        // 인용한 course 인덱스
        Long courseId = request.courseId();

        Course originalCourse = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        Course copyCourse = Course.builder()
                .type(originalCourse.getType())
                .title(originalCourse.getTitle())
                .createdAt(originalCourse.getCreatedAt())
                .originalCreatorId(originalCourse.getOriginalCreatorId())
                .originalCourseId(courseId)
                .build();

        courseRepository.save(copyCourse);

        /* 2. Planner 테이블에 필드 생성 */
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        Planner planner = Planner.builder()
                .user(user)
                .course(copyCourse)
                .startAt(LocalDate.now())
                .isDeleted(false)
                .isPrivate(true)
//                .summary()
//                .time()
                .isCompleted(false)
                .build();

        plannerRepository.save(planner);

        /* 3. 게시글을 올린 user를 코스 참가자로 등록 */
        Participant participant = Participant.builder()
                .user(user)
                .planner(planner)
                .approved(true)
                .isDeleted(false)
                .build();

        participantRepository.save(participant);

        /* 4. Posts 테이블에 저장*/
        Posts posts = Posts.builder()
                .user(user)
                .postType(PostType.COURSE)
                .planner(planner)
                .title(request.title())
                .content(request.content())
                .companionsNum(request.companionsNum())
                .createdAt(LocalDate.now())
                .isFinished(false)
                .isDeleted(false)
                .build();


        postsRepository.save(posts);

        return getResponse(posts, request.courseId());
    }

    @Transactional
    public CoursePostResponse update(Long postId, CoursePostUpdateRequest request) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(request.title())) {
            post = post.withTitle(request.title());

        }

        if (validationUtil.validateStringValue(request.content())) {
            post = post.withContent(request.content());

        }

        if (validationUtil.validateLongValue(request.participantNum())) {
            post = post.withCompanionsNum(request.participantNum());
        }

        if (request.isFinished() != post.isFinished()) {
            post = post.withIsFinished(request.isFinished());
        }

        if (validationUtil.validateLongValue(request.plannerId())) {
            Planner planner = plannerRepository.findById(request.plannerId())
                    .orElseThrow(EntityNotFoundException::new);

            post = post.withPlanner(planner);
        }

        postsRepository.save(post);

        return getResponse(post, request.plannerId());
    }


    @Transactional
    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Posts updatedPosts = posts.withIsFinishedAndIsDeleted(true, true);
        postsRepository.save(updatedPosts);
    }

    private CoursePostResponse getResponse(Posts post, Long courseId) {
        CoursePostResponse coursePostResponse = new CoursePostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getUser().getProfile().getProfileImageUrl(),
                post.getUser().getNumberTag(),
                post.getTitle(),
                post.getCompanionsNum(),
                participantQueryService.countCourseParticipant(courseId),
                post.getContent()
        );

        return coursePostResponse;
    }

    private Posts findPostById(Long postId) {
        return postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found id=" + postId));
    }
}
