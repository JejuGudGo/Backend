package com.example.jejugudgo.domain.mygudgo.review.service;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.ReviewCreateRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.request.ReviewsRequest;
import com.example.jejugudgo.domain.mygudgo.review.dto.response.Review;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReview;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReviewCategory3;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReviewImage;
import com.example.jejugudgo.domain.mygudgo.review.enums.Category3Type;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewCategory3Repository;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewImageRepository;
import com.example.jejugudgo.domain.mygudgo.review.repository.UserReviewRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReviewService {
    private final UserReviewRepository userReviewRepository;
    private final UserReviewCategory3Repository userReviewCategory3Repository;
    private final UserReviewImageRepository userReviewImageRepository;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    private final String JEJU_GUDGO = CourseType.COURSE_TYPE01.getType();
    private final String OLLE = CourseType.COURSE_TYPE02.getType();
    private final String TRAIL = CourseType.COURSE_TYPE03.getType();

    private final CourseType JEJU_GUDGO_ENUM = CourseType.COURSE_TYPE01;
    private final CourseType OLLE_ENUM = CourseType.COURSE_TYPE02;
    private final CourseType TRAIL_ENUM = CourseType.COURSE_TYPE03;
    private final JejuGudgoCourseReviewService jejuGudgoCourseReviewService;

    public List<Review> getUserReviews(HttpServletRequest httpRequest, ReviewsRequest request) {
        List<UserReview> userReviews = getCourses(request);
        User user = getUser(httpRequest);

        if (!userReviews.isEmpty()) {
            return userReviews.stream()
                    .map(review -> {
                        List<UserReviewCategory3> reviewCategory3s = userReviewCategory3Repository.findAllByReview(review);
                        List<String> tags = reviewCategory3s.stream()
                                .map(tag ->  tag.getTitle().getCategory3())
                                .toList();

                        List<UserReviewImage> images = userReviewImageRepository.findAllByReview(review);
                        List<String> reviewImages = images.stream()
                                .map(UserReviewImage::getImageUrl)
                                .toList();

                        return new Review(
                                review.getId(),
                                user.getUserProfile().getProfileImageUrl(),
                                user.getNickname(),
                                review.getStar(),
                                review.getCreateAt(),
                                tags,
                                reviewImages.isEmpty() ? null : reviewImages,
                                review.getContent()
                        );
                    }).toList();
        }
        return List.of();
    }

    public void createUserReview(HttpServletRequest httpRequest, ReviewCreateRequest request, List<MultipartFile> images) {
        User user = getUser(httpRequest);
        if (request.courseType().equals(JEJU_GUDGO))
            jejuGudgoCourseReviewService.createUserReview(request, user, images);

        else if (request.courseType().equals(OLLE))
            jejuGudgoCourseReviewService.createUserReview(request, user, images);

        else if (request.courseType().equals(TRAIL))
            jejuGudgoCourseReviewService.createUserReview(request, user, images);
    }

    private List<UserReview> getCourses(ReviewsRequest request) {
        if (request.courseType().equals(JEJU_GUDGO))
            return userReviewRepository.findAllByReviewTypeAndTargetId(JEJU_GUDGO_ENUM, request.targetId());

        else if (request.courseType().equals(OLLE))
            return userReviewRepository.findAllByReviewTypeAndTargetId(OLLE_ENUM, request.targetId());

        else if (request.courseType().equals(TRAIL))
            return userReviewRepository.findAllByReviewTypeAndTargetId(TRAIL_ENUM, request.targetId());
        return null;
    }

    private User getUser(HttpServletRequest httpRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        // TODO : 나중에 다른값들도 똑같이 변경하기
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE13));
    }
}
