package com.example.jejugudgo.domain.search.service;

import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.review.repository.ReviewCustomRepository;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import com.example.jejugudgo.domain.search.dto.sub.CourseBasicResponse;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.util.BookmarkUtil;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrailSearchDetailService {
    private final TrailRepository trailRepository;
    private final ReviewCustomRepository reviewCustomRepository;
    private final BookmarkUtil bookmarkUtil;
    private final ReviewCounter reviewCounter;

    public SearchDetailResponse getTrailDetail(HttpServletRequest request, Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        CourseBasicResponse courseBasicResponse = getCourseBasicResponse(request, trail);
        List<TopFiveRankedKeywordResponse> keywords = reviewCustomRepository.getTopCategoriesForCourse(ReviewType.TRAIL, trailId);

        return getSearchDetailResponse(courseBasicResponse, keywords);
    }

    private CourseBasicResponse getCourseBasicResponse(HttpServletRequest request, Trail trail) {
        List<String> tags = new ArrayList<>();
        tags.add(trail.getTrailType().getCode());

        return new CourseBasicResponse(
                trail.getId(),
                "산책로",
                tags,
                bookmarkUtil.isBookmarked(request, BookmarkType.TRAIL, trail.getId()),
                trail.getImageUrl(),
                trail.getTitle(),
                trail.getContent(),
                null,
                null,
                trail.getStarAvg(),
                reviewCounter.getReviewCount(ReviewType.TRAIL, trail.getId())
        );
    }

    private SearchDetailResponse getSearchDetailResponse(CourseBasicResponse courseBasicResponse, List<TopFiveRankedKeywordResponse> keywords) {
        return new SearchDetailResponse(
                courseBasicResponse,
                null,
                null,
                keywords
        );
    }
}
