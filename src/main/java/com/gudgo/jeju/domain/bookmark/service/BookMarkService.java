package com.gudgo.jeju.domain.bookmark.service;

import com.gudgo.jeju.domain.bookmark.dto.request.BookmarkCreateRequestDto;
import com.gudgo.jeju.domain.bookmark.dto.request.FilterDto;
import com.gudgo.jeju.domain.bookmark.dto.response.BookMarkResponseDto;
import com.gudgo.jeju.domain.bookmark.entity.BookMark;
import com.gudgo.jeju.domain.bookmark.entity.QBookMark;
import com.gudgo.jeju.domain.bookmark.query.BookmarkQueryService;
import com.gudgo.jeju.domain.bookmark.repository.BookMarkRepository;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.entity.QCourse;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerTagRepository;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.trail.entity.QTrail;
import com.gudgo.jeju.domain.trail.entity.Trail;
import com.gudgo.jeju.domain.trail.entity.TrailType;
import com.gudgo.jeju.domain.trail.repository.TrailRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.getUser;

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
    private final TrailRepository trailRepository;
    private final BookmarkQueryService bookmarkQueryService;


//    @Transactional
//    public void create(HttpServletRequest request, BookmarkCreateRequestDto requestDto) {
//        Long plannerId = requestDto.plannerId();
//        Planner planner = plannerRepository.findById(plannerId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        User user = getUser(request);
//
//        BookMark bookMark = BookMark.builder()
//                .user(user)
//                .planner(planner)
//                .build();
//
//        bookMarkRepository.save(bookMark);
//    }


    @Transactional
    public void create(HttpServletRequest request, BookmarkCreateRequestDto requestDto) {
        User user = getUser(request);

        BookMark bookMark;
        if (requestDto.plannerId() != null) {
            Planner planner = plannerRepository.findById(requestDto.plannerId())
                    .orElseThrow(EntityNotFoundException::new);
            bookMark = BookMark.builder()
                    .user(user)
                    .planner(planner)
                    .build();
        } else if (requestDto.trailId() != null) {
            Trail trail = trailRepository.findById(requestDto.trailId())
                    .orElseThrow(EntityNotFoundException::new);
            bookMark = BookMark.builder()
                    .user(user)
                    .trail(trail)
                    .build();
        } else {
            throw new IllegalArgumentException("Either plannerId or trailId must be provided");
        }

        bookMarkRepository.save(bookMark);
    }

    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        return userRepository.findById(userid)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<?> get(HttpServletRequest request, FilterDto filter, Pageable pageable) {
        Long userId = getUser(request).getId();
        return bookmarkQueryService.get(userId, filter, pageable);
    }



    public void delete(Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(EntityNotFoundException::new);

        bookMark = bookMark.withDeleted(bookMarkId, true);

        bookMarkRepository.save(bookMark);
    }
}
