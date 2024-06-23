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

    private final ValidationUtil validationUtil;


    public CoursePostResponse getCoursePost(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("posts not found id=" + postId));

        Long courseId = findPostById(postId).getCourse().getId();
        Long currentParticipantNum = participantRepository.countByCourseIdAndApprovedTrue(courseId);

        return new CoursePostResponse(
                posts.getId(),
                posts.getUser().getId(),
                posts.getUser().getNickname(),
                posts.getUser().getProfile().getProfileImageUrl(),
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

        course = course.withPostId(posts.getId());

        postsRepository.save(posts);
        courseRepository.save(course);

        return getResponse(posts);
    }

    @Transactional
    public CoursePostResponse update(Long postId, CoursePostUpdateRequest request) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(request.title())) {
            posts = posts.withTitle(request.title());

        }

        if (validationUtil.validateStringValue(request.content())) {
            posts = posts.withContent(request.content());

        }

        if (validationUtil.validateLongValue(request.courseId())) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(EntityNotFoundException::new);

            posts = posts.withCourse(course);

        }

        if (validationUtil.validateLongValue(request.participantNum())) {
            posts = posts.withCompanionsNum(request.participantNum());
        }

        if (request.isFinished() != posts.isFinished()) {
            posts = posts.withIsFinished(request.isFinished());
        }

        return getResponse(postsRepository.save(posts));
    }


    @Transactional
    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Posts updatedPosts = posts.withIsFinishedAndIsDeleted(true, true);
        postsRepository.save(updatedPosts);
    }

    private CoursePostResponse getResponse(Posts posts) {
        CoursePostResponse coursePostResponse = new CoursePostResponse(
                posts.getId(),
                posts.getUser().getId(),
                posts.getUser().getNickname(),
                posts.getUser().getProfile().getProfileImageUrl(),
                posts.getUser().getNumberTag(),
                posts.getCourse().getId(),
                posts.getTitle(),
                posts.getCompanionsNum(),
                participantQueryService.countCourseParticipant(posts.getCourse().getId()),
                posts.getContent()
        );

        return coursePostResponse;
    }

    private Posts findPostById(Long postId) {
        return postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found id=" + postId));
    }
}
