package com.gudgo.jeju.domain.bookmark.service;

import com.gudgo.jeju.domain.bookmark.dto.request.BookmarkCreateRequestDto;
import com.gudgo.jeju.domain.bookmark.dto.response.BookMarkResponseDto;
import com.gudgo.jeju.domain.bookmark.entity.BookMark;
import com.gudgo.jeju.domain.bookmark.repository.BookMarkRepository;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerTagResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerTagRepository;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookMarkService {
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;

    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final BookMarkRepository bookMarkRepository;

    private final PlannerTagRepository plannerTagRepository;
    private final CourseRepository courseRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final ReviewQueryService reviewQueryService;


    @Transactional
    public void create(HttpServletRequest request, BookmarkCreateRequestDto requestDto) {
        Long plannerId = requestDto.plannerId();
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        User user = getUser(request);

        BookMark bookMark = BookMark.builder()
                .user(user)
                .planner(planner)
                .build();

        bookMarkRepository.save(bookMark);
    }

    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        return userRepository.findById(userid)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<BookMarkResponseDto> get(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<BookMark> bookMarks = bookMarkRepository.findByUserIdAndIsDeletedFalse(userId);

        return bookMarks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookMarkResponseDto convertToDto(BookMark bookMark) {
        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(
                bookMark.getUser().getId(),
                bookMark.getUser().getEmail(),
                bookMark.getUser().getNickname(),
                bookMark.getUser().getName(),
                bookMark.getUser().getNumberTag(),
                bookMark.getUser().getProfile().getProfileImageUrl(),
                bookMark.getUser().getRole()
        );

        Planner planner = bookMark.getPlanner();
        List<String> tagResponses = plannerTagRepository.findByPlanner(planner).stream()
                .map(tag -> tag.getCode().toString())
                .collect(Collectors.toList());

        Course course = courseRepository.findById(planner.getCourse().getId())
                .orElseThrow(EntityNotFoundException::new);


        String distance = null;
        if (course.getType().equals(CourseType.JEJU) || course.getType().equals(CourseType.HAYOUNG)) {
            JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(course.getId())
                    .orElseThrow(EntityNotFoundException::new);
            distance = jeJuOlleCourse.getTotalDistance();

        }

        Long reviewCount = reviewQueryService.getUserCourseReviewCount(course.getId());

        PlannerListResponse plannerListResponse = new PlannerListResponse(
                planner.getId(),
                planner.getCourse().getContent(),
                distance,
                planner.getTime(),
                planner.getCourse().getStarAvg(),
                reviewCount,
                planner.isCompleted(),
                planner.isPrivate(),
                tagResponses
        );

        return new BookMarkResponseDto(
                bookMark.getId(),
                userInfoDto,
                plannerListResponse
        );
    }


    public void delete(Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(EntityNotFoundException::new);

        bookMark = bookMark.withDeleted(bookMarkId, true);

        bookMarkRepository.save(bookMark);
    }
}
