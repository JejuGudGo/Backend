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


    public CoursePostResponse get(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        LocalDate courseStartDate = posts.getCourse().getStartAt();

        if (courseStartDate.isAfter(LocalDate.now()) && !posts.isFinished()) {
            posts.withIsFinished(true);

            CoursePostResponse coursePostResponse = getResponse(posts);

            return coursePostResponse;
        }

        return null;
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

        CoursePostResponse response = getResponse(posts);

        return response;
    }

    @Transactional
    public CoursePostResponse update(Long postId, CoursePostUpdateRequest request) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(request.title())) {
            posts.withTitle(request.title());

        }

        if (validationUtil.validateStringValue(request.content())) {
            posts.withContent(request.content());

        }

        if (validationUtil.validateLongValue(request.courseId())) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(EntityNotFoundException::new);

            posts.withCourse(course);

        }

        if (validationUtil.validateLongValue(request.participantNum())) {
            posts.withCompanionsNum(request.participantNum());
        }

        if (request.isFinished() != posts.isFinished()) {
            posts.withIsFinished(request.isFinished());
        }

        postsRepository.save(posts);

        CoursePostResponse response = getResponse(posts);

        return response;
    }

    @Transactional
    public void delete(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        posts.withIsFinished(true);

        postsRepository.save(posts);
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
}
