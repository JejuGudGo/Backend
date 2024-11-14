package com.example.jejugudgo.domain.user.myGudgo.bookmark.service;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.BookMarkRequest;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.response.BookMarkResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookMark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookMarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookMarkRepository;
import com.example.jejugudgo.domain.course.dto.response.JejuGudgoCourseResponseForList;
import com.example.jejugudgo.domain.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.course.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.olle.repository.JejuOlleSpotRepository;
import com.example.jejugudgo.domain.trail.dto.TrailResponseForList;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMarkService {

    // 필요한 리포지토리 및 유틸 클래스 선언
    private final BookMarkRepository bookMarkRepository;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final TrailRepository trailRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final JejuOlleCourseRepository jejuOlleCourseRepository;
    private final JejuOlleSpotRepository jejuOlleSpotRepository;
    private final JejuGudgoCourseSpotRepository jejuGudgoCourseSpotRepository;

    // 사용자 북마크 조회 메서드
    public List<BookMarkResponse> getBookMarks(String query, HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);

        if ("전체".equals(query)) {
            return getAllBookmarks(userId);  // 모든 북마크 조회
        } else {
            return getBookmarksByType(userId, query);  // 특정 유형의 북마크 조회
        }
    }

    // 모든 북마크를 조회하여 BookMarkResponse 형태로 반환
    private List<BookMarkResponse> getAllBookmarks(Long userId) {
        List<BookMark> bookmarks = bookMarkRepository.findByUserId(userId);

        if (bookmarks.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE97);
        }

        return bookmarks.stream()
                .map(this::convertToBookMarkResponse)
                .collect(Collectors.toList());
    }


    // 특정 유형의 즐겨찾기만 조회하여 BookMarkResponse 형태로 반환
    private List<BookMarkResponse> getBookmarksByType(Long userId, String typeCode) {
        BookMarkType bookMarkType = BookMarkType.fromCode(typeCode);

        // 존재하지 않는 북마크 유형일 경우 RETCODE_10 예외 발생
        if (bookMarkType == null) {
            throw new CustomException(RetCode.RET_CODE10);
        }

        return bookMarkRepository.findByUserIdAndBookMarkType(userId, bookMarkType).stream()
                .map(this::convertToBookMarkResponse)
                .collect(Collectors.toList());
    }


    // BookMark 엔티티를 BookMarkResponse로 변환하는 메서드
    private BookMarkResponse convertToBookMarkResponse(BookMark bookmark) {
        switch (bookmark.getBookMarkType()) {
            case OLLE:
                return convertOlleBookmark(bookmark);  // 올레 코스 북마크 변환
            case JEJU_GUDGO:
                return convertGudgoBookmark(bookmark);  // 제주 굳고 코스 북마크 변환
            case TRAIL:
                return convertTrailBookmark(bookmark);  // 트레일 북마크 변환
            default:
                throw new CustomException(RetCode.RET_CODE10);  // 예외 처리
        }
    }

    private BookMarkResponse convertOlleBookmark(BookMark bookmark) {
        JejuOlleCourse jejuOlleCourse = jejuOlleCourseRepository.findById(bookmark.getTargetId())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return new BookMarkResponse(
                bookmark.getId(),
                mapToJejuOlleCourseResponse(jejuOlleCourse, jejuOlleCourse.getStartSpotTitle(), jejuOlleCourse.getEndSpotTitle()),
                null,
                null
        );
    }

    private BookMarkResponse convertGudgoBookmark(BookMark bookmark) {
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(bookmark.getTargetId())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        String startSpotTitleGudgo = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseIdOrderByIdAsc(jejuGudgoCourse.getId())
                .map(spot -> spot.getTitle())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        String endSpotTitleGudgo = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseIdOrderByIdDesc(jejuGudgoCourse.getId())
                .map(spot -> spot.getTitle())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return new BookMarkResponse(
                bookmark.getId(),
                null,
                mapToJejuGudgoCourseResponse(jejuGudgoCourse, startSpotTitleGudgo, endSpotTitleGudgo),
                null
        );
    }

    private BookMarkResponse convertTrailBookmark(BookMark bookmark) {
        Trail trail = trailRepository.findById(bookmark.getTargetId())
                .orElse(null);
        return new BookMarkResponse(
                bookmark.getId(),
                null,
                null,
                mapToTrailResponse(trail)
        );
    }

    // JejuOlleCourse 엔티티를 JejuOlleCourseResponseForList로 매핑
    private JejuOlleCourseResponseForList mapToJejuOlleCourseResponse(JejuOlleCourse jejuOlleCourse, String startSpot, String endSpot) {
        if (jejuOlleCourse == null) return null;
        return new JejuOlleCourseResponseForList(
                jejuOlleCourse.getId(),
                jejuOlleCourse.getTitle(),
                startSpot,
                endSpot,
                jejuOlleCourse.getTotalTime(),
                jejuOlleCourse.getStarAvg(),
                0L
        );
    }

    // JejuGudgoCourse 엔티티를 JejuGudgoCourseResponseForList로 매핑
    private JejuGudgoCourseResponseForList mapToJejuGudgoCourseResponse(JejuGudgoCourse jejuGudgoCourse, String startSpot, String endSpot) {
        if (jejuGudgoCourse == null) return null;
        return new JejuGudgoCourseResponseForList(
                jejuGudgoCourse.getId(),
                jejuGudgoCourse.getTitle(),
                startSpot,
                endSpot,
                jejuGudgoCourse.getTotalTime(),
                jejuGudgoCourse.getStarAvg(),
                0L
        );
    }

    // Trail 엔티티를 TrailResponseForList로 매핑
    private TrailResponseForList mapToTrailResponse(Trail trail) {
        if (trail == null) return null;
        return new TrailResponseForList(
                trail.getId(),
                trail.getTitle(),
                trail.getStarAvg(),
                0L
        );
    }

    @Transactional
    public void create(HttpServletRequest servletRequest, BookMarkRequest bookMarkRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

        BookMarkType bookMarkType = BookMarkType.fromCode(bookMarkRequest.code());

        if (bookMarkType == null) {
            throw new CustomException(RetCode.RET_CODE10);
        }

        // 새로운 북마크 생성 및 저장
        BookMark bookMark = BookMark.builder()
                .user(user)
                .bookMarkType(bookMarkType)
                .targetId(bookMarkRequest.targetId())
                .build();

        bookMarkRepository.save(bookMark);
    }

    @Transactional
    public void delete(Long bookMarkId, HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);

        // 해당 ID의 북마크가 사용자와 일치하는지 확인 후 삭제
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .filter(b -> b.getUser().getId().equals(userId))
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        bookMarkRepository.delete(bookMark);
    }
}
