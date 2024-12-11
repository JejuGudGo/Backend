package com.example.jejugudgo.domain.mygudgo.review.service;

import com.example.jejugudgo.domain.mygudgo.review.dto.request.ReviewCreateRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.ReviewsRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.UnReviewedDateRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.response.Review;
import com.example.jejugudgo.domain.mygudgo.review.dto.response.UnReviewedDateResponse;
import com.example.jejugudgo.domain.user.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OlleCourseReviewService implements ReviewService{

    @Override
    public UnReviewedDateResponse getUnReviewedData(UnReviewedDateRequest request, User user) {
        return null;
    }

    @Override
    public void createUserReview(ReviewCreateRequest request, User user, List<MultipartFile> images) {

    }
}
