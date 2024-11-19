package com.example.jejugudgo.domain.review.util;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.review.repository.ReviewRepository;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
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

    public int getJejuGudgoReviewcount(ReviewType type, Long id) {
        if (type.equals(ReviewType.JEJUGUDGO))
            return getJejuGudgoCourseReviewCount(id);

        else if (type.equals(ReviewType.OLLE))
            return getJejuOlleCourseReviewCount(id);

        else if (type.equals(ReviewType.TRAIL))
            return getTrailReviewCount(id);

        return 0;
    }

    private int getJejuGudgoCourseReviewCount(Long id) {
        JejuGudgoCourse course = jejuGudgoCourseRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return reviewRepository.findDistinctByJejuGudgoCourse(course).size();
    }

    private int getJejuOlleCourseReviewCount(Long id) {
        JejuOlleCourse course = jejuOlleCourseRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return reviewRepository.findDistinctByJejuOlleCourse(course).size();
    }

    private int getTrailReviewCount(Long id) {
        Trail trail = trailRepository.findById(id)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return reviewRepository.findDistinctByTrail(trail).size();
    }
}
