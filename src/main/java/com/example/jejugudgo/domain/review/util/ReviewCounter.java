package com.example.jejugudgo.domain.review.util;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.review.repository.ReviewRepository;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewCounter {
    private final ReviewRepository reviewRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final JejuOlleCourseRepository jejuOlleCourseRepository;
    private final TrailRepository trailRepository;

    public Long getReviewCount(BookmarkType type, Long id) {
        if (type.equals(BookmarkType.JEJU_GUDGO))
            return getJejuGudgoCourseReviewCount(id);

        else if (type.equals(BookmarkType.OLLE))
            return getJejuOlleCourseReviewCount(id);

        else if (type.equals(BookmarkType.TRAIL))
            return getTrailReviewCount(id);

        return null;
    }

    private Long getJejuGudgoCourseReviewCount(Long id) {
        JejuGudgoCourse course = jejuGudgoCourseRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        int size = reviewRepository.findDistinctByJejuGudgoCourse(course).size();
        return size == 0 ? null : Long.parseLong(String.valueOf(size));
    }

    private Long getJejuOlleCourseReviewCount(Long id) {
        JejuOlleCourse course = jejuOlleCourseRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        int size = reviewRepository.findDistinctByJejuOlleCourse(course).size();
        return size == 0 ? null : Long.parseLong(String.valueOf(size));
    }

    private Long getTrailReviewCount(Long id) {
        Trail trail = trailRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        int size = reviewRepository.findDistinctByTrail(trail).size();
        return size == 0 ? null : Long.parseLong(String.valueOf(size));
    }
}
