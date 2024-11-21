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

        return new CourseBasicResponse (
                jejuGudgoCourse.getId(),
                "제주걷고",
                tags,
                bookmarkUtil.isBookmarked(request, BookmarkType.JEJU_GUDGO, jejuGudgoCourse.getId()),
                jejuGudgoCourse.getImageUrl(),
                jejuGudgoCourse.getTitle(),
                jejuGudgoCourse.getSummary(),
                jejuGudgoCourse.getDistance(),
                jejuGudgoCourse.getTime(),
                jejuGudgoCourse.getStarAvg(),
                reviewCounter.getReviewCount(ReviewType.JEJU_GUDGO, jejuGudgoCourse.getId())
        );
    }

    private JeujuGudgoCourseInfoResponse getJejuGodgoCourseInfo(JejuGudgoCourse jejuGudgoCourse) {
        // 1. 스팟 리스트 가져오기
        List<JejuGudgoCourseSpot> spots = jejuGudgoCourseSpotRepository.findByJejuGudgoCourseOrderByOrderNumberAsc(jejuGudgoCourse);
        List<SpotResponse> spotResponses = spots.stream()
                .map(spot -> new SpotResponse(
                        spot.getId(),
                        spot.getTitle(),
                        spot.getOrderNumber(),
                        spot.getLatitude(),
                        spot.getLongitude()
                )).toList();

        // 2. 시작 스팟 정보
        SpotResponse startSpot = new SpotResponse(
                null, // ID는 필요하지 않으므로 null 처리
                jejuGudgoCourse.getStartSpotTitle(),
                1L, // 순서는 항상 1로 가정
                jejuGudgoCourse.getStartLatitude(),
                jejuGudgoCourse.getStartLongitude()
        );

        // 3. 종료 스팟 정보
        SpotResponse endSpot = new SpotResponse(
                null, // ID는 필요하지 않으므로 null 처리
                jejuGudgoCourse.getEndSpotTitle(),
                (long) spots.size(), // 순서는 전체 스팟 수로 설정
                jejuGudgoCourse.getEndLatitude(),
                jejuGudgoCourse.getEndLongitude()
        );

        // 4. 응답 객체 생성 및 반환
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
                keywords
        );
    }
}
