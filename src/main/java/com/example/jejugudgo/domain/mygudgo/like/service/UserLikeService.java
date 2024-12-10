package com.example.jejugudgo.domain.mygudgo.like.service;

import com.example.jejugudgo.domain.course.common.dto.response.CourseListResponse;
import com.example.jejugudgo.domain.course.common.dto.response.TrailListResponse;
import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.common.query.CourseQueryService;
import com.example.jejugudgo.domain.course.common.query.TrailQueryService;
import com.example.jejugudgo.domain.mygudgo.like.dto.request.UserLikeRequest;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.UserLikeResponse;
import com.example.jejugudgo.domain.mygudgo.like.entity.UserLike;
import com.example.jejugudgo.domain.mygudgo.like.repository.UserLikeRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final TokenUtil tokenUtil; // JWT 토큰 유틸리티
    private final UserLikeRepository userLikeRepository; // UserLike 레포지토리
    private final UserRepository userRepository; // User 레포지토리
    private final CourseQueryService courseQueryService; // Course 데이터 조회 서비스
    private final TrailQueryService trailQueryService; // Trail 데이터 조회 서비스

    /**
     * 사용자 좋아요 리스트 조회
     */
    public Page<UserLikeResponse> getUserLikes(String query, HttpServletRequest request, Pageable pageable) {
        Long userId = tokenUtil.getUserIdFromHeader(request); // 토큰에서 사용자 ID 추출

        // "전체" 조회 요청 처리
        if ("전체".equals(query)) {
            return getAllUserLikes(userId, pageable);
        }

        // 특정 타입 조회 처리
        return getUserLikesByType(userId, query, pageable);
    }

    /**
     * 좋아요 생성
     */
    @Transactional
    public LikeInfo create(HttpServletRequest servletRequest, UserLikeRequest userLikeRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest); // 토큰에서 사용자 ID 추출
        User user = findUserById(userId);

        // 코스타입 유효성 검사
        CourseType courseType = validateCourseType(userLikeRequest.cat1());

        // 중복 좋아요 검사
        validateDuplicateLike(user, courseType, userLikeRequest.targetId());

        // 좋아요 저장
        UserLike userLike = saveUserLike(user, courseType, userLikeRequest.targetId());

        // LikeInfo 생성
        return new LikeInfo(
                true,
                courseType.getType(),
                userLike.getId()
        );
    }

    /**
     * 좋아요 삭제
     */
    @Transactional
    public void delete(UserLikeRequest userLikeRequest, HttpServletRequest servletRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest); // 토큰에서 사용자 ID 추출
        UserLike userLike = findUserLikeById(userLikeRequest.targetId());

        // 삭제 권한 검사
        validateDeletePermission(userLike, userId);

        userLikeRepository.delete(userLike); // 좋아요 삭제
    }

    /**
     * UserLike를 UserLikeResponse로 변환
     */
    private UserLikeResponse mapToResponse(UserLike userLike) {
        CourseListResponse courseForList = null;
        TrailListResponse trailForList = null;

        if (userLike.getCourseType() == CourseType.COURSE_TYPE01 || userLike.getCourseType() == CourseType.COURSE_TYPE02) {
            courseForList = courseQueryService.getCourseForList(userLike.getTargetId());
        } else if (userLike.getCourseType() == CourseType.COURSE_TYPE03) {
            trailForList = trailQueryService.getTrailForList(userLike.getTargetId());
        }

        return new UserLikeResponse(
                userLike.getId(),
                courseForList,
                trailForList
        );
    }

    /**
     * 사용자 좋아요 전체 조회
     */
    private Page<UserLikeResponse> getAllUserLikes(Long userId, Pageable pageable) {
        Page<UserLike> userLikesPage = userLikeRepository.findByUserId(userId, pageable);
        return userLikesPage.map(this::mapToResponse);
    }

    /**
     * 특정 타입의 좋아요 조회
     */
    private Page<UserLikeResponse> getUserLikesByType(Long userId, String query, Pageable pageable) {
        CourseType courseType = validateCourseType(query);

        Page<UserLike> userLikesPage = userLikeRepository.findByUserIdAndCourseType(userId, courseType, pageable);
        return userLikesPage.map(this::mapToResponse);
    }


    /**
     * 유효한 사용자 반환
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97)); // 사용자 없을 경우 예외
    }

    /**
     * 유효한 CourseType 반환
     */
    private CourseType validateCourseType(String courseTypeStr) {
        CourseType courseType = CourseType.fromCat1(courseTypeStr);
        if (courseType == null) {
            throw new CustomException(RetCode.RET_CODE14); // 존재하지 않는 코스타입
        }
        return courseType;
    }

    /**
     * 중복 좋아요 검사
     */
    private void validateDuplicateLike(User user, CourseType courseType, Long targetId) {
        boolean alreadyLiked = userLikeRepository
                .findByUserAndCourseTypeAndTargetId(user, courseType, targetId)
                .isPresent();
        if (alreadyLiked) {
            throw new CustomException(RetCode.RET_CODE16); // 중복 좋아요 예외
        }
    }

    /**
     * 좋아요 저장
     */
    private UserLike saveUserLike(User user, CourseType courseType, Long targetId) {
        UserLike userLike = UserLike.builder()
                .user(user)
                .courseType(courseType)
                .targetId(targetId)
                .build();
        return userLikeRepository.save(userLike);
    }

    /**
     * UserLike 엔티티 찾기
     */
    private UserLike findUserLikeById(Long userLikeId) {
        return userLikeRepository.findById(userLikeId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97)); // 존재하지 않는 좋아요
    }

    /**
     * 삭제 권한 검사
     */
    private void validateDeletePermission(UserLike userLike, Long userId) {
        if (!userLike.getUser().getId().equals(userId)) {
            throw new CustomException(RetCode.RET_CODE15); // 권한 없는 사용자 예외
        }
    }
}
