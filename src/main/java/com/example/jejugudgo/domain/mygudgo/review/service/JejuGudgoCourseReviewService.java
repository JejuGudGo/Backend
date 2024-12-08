package com.example.jejugudgo.domain.mygudgo.review.service;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.common.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.ReviewCreateRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.UnReviewedDateRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.response.UnReviewedDateResponse;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewCategory3Repository;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewImageRepository;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseReviewService implements ReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserReviewCategory3Repository userReviewCategory3Repository;
    private final UserReviewImageRepository userReviewImageRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;

    private final CourseType JEJU_GUDGO = CourseType.COURSE_TYPE01;

    @Override
    public UnReviewedDateResponse getUnReviewedData(UnReviewedDateRequest request, User user) {
        return null;
    }

    @Override
    public void createUserReview(ReviewCreateRequest request, User user, List<MultipartFile> images) {

    }
}
