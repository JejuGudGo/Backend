package com.example.jejugudgo.domain.search.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.review.repository.ReviewCustomRepository;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import com.example.jejugudgo.domain.search.dto.sub.CourseBasicResponse;
import com.example.jejugudgo.domain.search.dto.sub.JeujuGudgoCourseInfoResponse;
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
public class JejuGudgoSearchDetailService {
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final JejuGudgoCourseTagRepository jejuGudgoCourseTagRepository;
    private final JejuGudgoCourseSpotRepository jejuGudgoCourseSpotRepository;
    private final ReviewCustomRepository reviewCustomRepository;
    private final BookmarkUtil bookmarkUtil;
    private final ReviewCounter reviewCounter;

    public SearchDetailResponse getJejuGudgoCourseDetail(HttpServletRequest request, Long courseId) {
       JejuGudgoCourse jejuGudgoCourse =  jejuGudgoCourseRepository.findById(courseId)
               .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        CourseBasicResponse courseBasicResponse = getCourseBasicResponse(request, jejuGudgoCourse);
        JeujuGudgoCourseInfoResponse infoResponse = getJejuGodgoCourseInfo(jejuGudgoCourse);
        List<TopFiveRankedKeywordResponse> keywords = reviewCustomRepository.getTopCategoriesForCourse(ReviewType.JEJU_GUDGO, courseId);

        return getSearchDetailResponse(courseBasicResponse, infoResponse, keywords);
    }

    private CourseBasicResponse getCourseBasicResponse(HttpServletRequest request, JejuGudgoCourse jejuGudgoCourse) {
        List<JejuGudgoCourseTag> courseTags = jejuGudgoCourseTagRepository.findByJejuGudgoCourse(jejuGudgoCourse);
        List<String> tags = courseTags.stream()
                .map(tag -> tag.getCourseTag().getTag())
                .toList();

        Bookmark bookmark =  bookmarkUtil
                .isBookmarked(request, BookmarkType.JEJU_GUDGO, jejuGudgoCourse.getId());

        Double starAvg = jejuGudgoCourse.getStarAvg();

        return new CourseBasicResponse (
                jejuGudgoCourse.getId(),
                BookmarkType.JEJU_GUDGO.getCode(),
                tags,
                bookmark != null,
                bookmark != null ? bookmark.getId() : null,
                jejuGudgoCourse.getImageUrl(),
                jejuGudgoCourse.getTitle(),
                jejuGudgoCourse.getStartSpotTitle() + "-" + jejuGudgoCourse.getEndSpotTitle(),
                null,
                jejuGudgoCourse.getDistance(),
                jejuGudgoCourse.getTime(),
                starAvg == 0.0 ? null : starAvg,
                reviewCounter.getReviewCount(BookmarkType.JEJU_GUDGO, jejuGudgoCourse.getId())
        );
    }

    private JeujuGudgoCourseInfoResponse getJejuGodgoCourseInfo(JejuGudgoCourse jejuGudgoCourse) {
        List<JejuGudgoCourseSpot> spots = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseOrderByOrderNumberAsc(jejuGudgoCourse);
        List<SpotResponse> spotResponses = spots.stream()
                .map(response -> new SpotResponse (
                        response.getId(),
                        response.getTitle(),
                        response.getOrderNumber(),
                        response.getLatitude(),
                        response.getLongitude()
                        )
                ).toList();

        SpotResponse startSpot = new SpotResponse (
                null,
                jejuGudgoCourse.getStartSpotTitle(),
                null,
                jejuGudgoCourse.getStartLatitude(),
                jejuGudgoCourse.getEndLongitude()
        );

        SpotResponse endSpot = new SpotResponse (
                null,
                jejuGudgoCourse.getEndSpotTitle(),
                null,
                jejuGudgoCourse.getEndLatitude(),
                jejuGudgoCourse.getEndLongitude()
        );

        return new JeujuGudgoCourseInfoResponse(
                spotResponses,
                jejuGudgoCourse.getContent(),
                startSpot,
                endSpot
        );
    }

    private SearchDetailResponse getSearchDetailResponse(
            CourseBasicResponse courseBasicResponse,
            JeujuGudgoCourseInfoResponse jeujuGudgoCourseInfoResponse,
            List<TopFiveRankedKeywordResponse> keywords
    ) {
        return new SearchDetailResponse(
                courseBasicResponse,
                jeujuGudgoCourseInfoResponse,
                null,
                keywords.isEmpty() ? null : keywords
        );
    }
}
