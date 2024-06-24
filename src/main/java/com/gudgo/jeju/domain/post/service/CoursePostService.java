package com.gudgo.jeju.domain.post.service;

import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.query.ParticipantQueryService;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
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

    private final ValidationUtil validationUtil;


    public CoursePostResponse getCoursePost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("posts not found id=" + postId));

        Course course = courseRepository.findByPostId(post.getId());

        Long currentParticipantNum = participantQueryService.countCourseParticipant(course.getId());

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
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(EntityNotFoundException::new);

        Posts posts = Posts.builder()
                .user(user)
                .postType(PostType.COURSE)
                .title(request.title())
                .content(request.content())
                .companionsNum(request.companionsNum())
                .createdAt(LocalDate.now())
                .isFinished(false)
                .isDeleted(false)
                .build();

        course = course.withPost(posts);

        postsRepository.save(posts);
        courseRepository.save(course);

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

        if (validationUtil.validateLongValue(request.courseId())) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(EntityNotFoundException::new);

            course.withPost(post);
            courseRepository.save(course);
        }

        postsRepository.save(post);

        return getResponse(post, request.courseId());
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
