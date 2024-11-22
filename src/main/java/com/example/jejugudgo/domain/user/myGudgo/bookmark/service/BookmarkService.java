package com.example.jejugudgo.domain.user.myGudgo.bookmark.service;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.BookmarkRequest;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.response.BookmarkResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseResponseForList;
import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
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
public class BookmarkService {

    // 필요한 리포지토리 및 유틸 클래스 선언
    private final BookmarkRepository bookMarkRepository;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final TrailRepository trailRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final JejuOlleCourseRepository jejuOlleCourseRepository;
    private final JejuGudgoCourseSpotRepository jejuGudgoCourseSpotRepository;

    // 사용자 북마크 조회 메서드
    public List<BookmarkResponse> getBookMarks(String query, HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);

        if ("전체".equals(query)) {
            return getAllBookmarks(userId);  // 모든 북마크 조회
        } else {
            return getBookmarksByType(userId, query);  // 특정 유형의 북마크 조회
        }
    }

    // 모든 북마크를 조회하여 BookMarkResponse 형태로 반환
    private List<BookmarkResponse> getAllBookmarks(Long userId) {
        List<Bookmark> bookmarks = bookMarkRepository.findByUserId(userId);

        if (bookmarks.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE97);
        }

        return bookmarks.stream()
                .map(this::convertToBookMarkResponse)
                .collect(Collectors.toList());
    }


    // 특정 유형의 즐겨찾기만 조회하여 BookMarkResponse 형태로 반환
    private List<BookmarkResponse> getBookmarksByType(Long userId, String typeCode) {
        BookmarkType bookMarkType = BookmarkType.fromCode(typeCode);

        // 존재하지 않는 북마크 유형일 경우 RETCODE_10 예외 발생
        if (bookMarkType == null) {
            throw new CustomException(RetCode.RET_CODE10);
        }

        return bookMarkRepository.findByUserIdAndBookMarkType(userId, bookMarkType).stream()
                .map(this::convertToBookMarkResponse)
                .collect(Collectors.toList());
    }


    // BookMark 엔티티를 BookMarkResponse로 변환하는 메서드
    private BookmarkResponse convertToBookMarkResponse(Bookmark bookmark) {
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

    private BookmarkResponse convertOlleBookmark(Bookmark bookmark) {
        JejuOlleCourse jejuOlleCourse = jejuOlleCourseRepository.findById(bookmark.getTargetId())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return new BookmarkResponse(
                bookmark.getId(),
                mapToJejuOlleCourseResponse(jejuOlleCourse, jejuOlleCourse.getStartSpotTitle(), jejuOlleCourse.getEndSpotTitle()),
                null,
                null
        );
    }

    private BookmarkResponse convertGudgoBookmark(Bookmark bookmark) {
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(bookmark.getTargetId())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        String startSpotTitleGudgo = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseIdOrderByIdAsc(jejuGudgoCourse.getId())
                .map(spot -> spot.getTitle())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        String endSpotTitleGudgo = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseIdOrderByIdDesc(jejuGudgoCourse.getId())
                .map(spot -> spot.getTitle())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return new BookmarkResponse(
                bookmark.getId(),
                null,
                mapToJejuGudgoCourseResponse(jejuGudgoCourse, startSpotTitleGudgo, endSpotTitleGudgo),
                null
        );
    }

    private BookmarkResponse convertTrailBookmark(Bookmark bookmark) {
        Trail trail = trailRepository.findById(bookmark.getTargetId())
                .orElse(null);
        return new BookmarkResponse(
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
                jejuOlleCourse.getTime(),
                jejuOlleCourse.getStarAvg(),
                0
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
                jejuGudgoCourse.getTime(),
                jejuGudgoCourse.getDistance(),
                jejuGudgoCourse.getStarAvg(),
                0
        );
    }

    // Trail 엔티티를 TrailResponseForList로 매핑
    private TrailResponseForList mapToTrailResponse(Trail trail) {
        if (trail == null) return null;
        return new TrailResponseForList(
                trail.getId(),
                trail.getTitle(),
                trail.getStarAvg(),
                0
        );
    }

    @Transactional
    public void create(HttpServletRequest servletRequest, BookmarkRequest bookMarkRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

        BookmarkType bookMarkType = BookmarkType.fromCode(bookMarkRequest.code());

        // 새로운 북마크 생성 및 저장
        Bookmark bookMark = Bookmark.builder()
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
        Bookmark bookMark = bookMarkRepository.findById(bookMarkId)
                .filter(b -> b.getUser().getId().equals(userId))
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        bookMarkRepository.delete(bookMark);
    }
}
