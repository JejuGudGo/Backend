package com.example.jejugudgo.domain.mygudgo.like.util;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.entity.UserLike;
import com.example.jejugudgo.domain.mygudgo.like.repository.UserLikeRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLikeUtil {
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;
    private final TokenUtil tokenUtil;

    private final CourseType JEJU_GUDGO = CourseType.COURSE_TYPE01;
    private final CourseType OLLE = CourseType.COURSE_TYPE02;
    private final CourseType TRAIL = CourseType.COURSE_TYPE03;

    public LikeInfo isLiked(HttpServletRequest request, String cat1, Long id) {
        CourseType courseType = CourseType.fromCat1(cat1);
        User user = findUser(request);

        if (courseType == JEJU_GUDGO)
            return isJejuGudgoCourseLiked(id, user);

        else if (courseType == OLLE)
            return isOlleCourseLiked(id, user);

        else if (courseType == TRAIL)
            return isTrailLiked(id, user);

        return null;
    }

    private LikeInfo isJejuGudgoCourseLiked(Long id, User user) {
        UserLike userLike =  userLikeRepository
                .findByUserAndCourseTypeAndTargetId(user, JEJU_GUDGO, id)
                .orElse(null);

        return new LikeInfo(
                userLike != null,
                CourseType.COURSE_TYPE01.getPinKeyType() + id
        );
    }

    private LikeInfo isOlleCourseLiked(Long id, User user) {
        UserLike userLike =  userLikeRepository
                .findByUserAndCourseTypeAndTargetId(user, OLLE, id)
                .orElse(null);

        return new LikeInfo(
                userLike != null,
                CourseType.COURSE_TYPE02.getPinKeyType() + id
        );
    }

    private LikeInfo isTrailLiked(Long id, User user) {
        UserLike userLike =  userLikeRepository
                .findByUserAndCourseTypeAndTargetId(user, TRAIL, id)
                .orElse(null);

        return new LikeInfo(
                userLike != null,
                CourseType.COURSE_TYPE03.getPinKeyType() + id
        );
    }

    private User findUser(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return user;
    }
}
