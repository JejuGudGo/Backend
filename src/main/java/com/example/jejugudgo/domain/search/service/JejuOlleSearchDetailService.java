package com.example.jejugudgo.domain.search.service;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourseTag;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleSpotRepository;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleTagRepository;
import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.review.repository.ReviewCustomRepository;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import com.example.jejugudgo.domain.search.dto.sub.CourseBasicResponse;
import com.example.jejugudgo.domain.search.dto.sub.OlleCourseInfoResponse;
import com.example.jejugudgo.domain.search.dto.sub.SpotResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.util.BookmarkUtil;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuOlleSearchDetailService {
    private final JejuOlleCourseRepository jejuOlleCourseRepository;
    private final JejuOlleTagRepository jejuOlleTagRepository;
    private final JejuOlleSpotRepository jejuOlleSpotRepository;
    private final ReviewCustomRepository reviewCustomRepository;
    private final BookmarkUtil bookmarkUtil;
    private final ReviewCounter reviewCounter;

    public SearchDetailResponse getJejuOlleCourseDetail(HttpServletRequest request, Long courseId) {
        JejuOlleCourse jejuOlleCourse = jejuOlleCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        CourseBasicResponse courseBasicResponse = getCourseBasicResponse(request, jejuOlleCourse);
        OlleCourseInfoResponse olleCourseInfoResponse = getJejuOlleCourseInfo(jejuOlleCourse);
        List<TopFiveRankedKeywordResponse> keywords = reviewCustomRepository.getTopCategoriesForCourse(ReviewType.OLLE, courseId);

        return getSearchDetailResponse(courseBasicResponse, olleCourseInfoResponse, keywords);
    }

    private CourseBasicResponse getCourseBasicResponse(HttpServletRequest request, JejuOlleCourse jejuOlleCourse) {
        List<JejuOlleCourseTag> courseTags = jejuOlleTagRepository.findByJejuOlleCourse(jejuOlleCourse);
        List<String> tags = courseTags.stream()
                .map(tag -> tag.getOlleTag().getTag())
                .toList();

        Bookmark bookmark =  bookmarkUtil
                .isBookmarked(request, BookmarkType.OLLE, jejuOlleCourse.getId());

        return new CourseBasicResponse(
                jejuOlleCourse.getId(),
                jejuOlleCourse.getOlleType().getType(),
                tags,
                bookmark != null,
                bookmark != null ? bookmark.getId() : null,
                jejuOlleCourse.getCourseImageUrl(),
                jejuOlleCourse.getTitle(),
                jejuOlleCourse.getSummary(),
                jejuOlleCourse.getDistance(),
                jejuOlleCourse.getTime(),
                jejuOlleCourse.getStarAvg(),
                reviewCounter.getReviewCount(BookmarkType.OLLE, jejuOlleCourse.getId())
        );
    }

    private OlleCourseInfoResponse getJejuOlleCourseInfo(JejuOlleCourse jejuOlleCourse) {
        List<JejuOlleSpot> spots = jejuOlleSpotRepository.findByJejuOlleCourse(jejuOlleCourse);
        List<SpotResponse> spotResponses = spots.stream()
                .map(response -> new SpotResponse (
                                response.getId(),
                                response.getTitle(),
                                response.getSpotOrder(),
                                response.getLatitude(),
                                response.getLongitude()
                        )
                ).toList();

        SpotResponse startSpot = new SpotResponse (
            null,
                jejuOlleCourse.getStartSpotTitle(),
                null,
                jejuOlleCourse.getStartLatitude(),
                jejuOlleCourse.getStartLongitude()
        );

        SpotResponse endSpot = new SpotResponse (
                null,
                jejuOlleCourse.getEndSpotTitle(),
                null,
                jejuOlleCourse.getEndLatitude(),
                jejuOlleCourse.getEndLongitude()
        );

        return new OlleCourseInfoResponse(
                spotResponses,
                jejuOlleCourse.getContent(),
                jejuOlleCourse.getInfoAddress(),
                jejuOlleCourse.getInfoOpenTime(),
                jejuOlleCourse.getInfoPhone(),
                startSpot,
                endSpot
        );
    }

    private SearchDetailResponse getSearchDetailResponse(
            CourseBasicResponse courseBasicResponse,
            OlleCourseInfoResponse olleCourseInfoResponse,
            List<TopFiveRankedKeywordResponse> keywords
    ) {
        return new SearchDetailResponse(
                courseBasicResponse,
                null,
                olleCourseInfoResponse,
                keywords
        );
    }
}
