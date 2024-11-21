package com.example.jejugudgo.domain.course.jejugudgo.util;

import com.example.jejugudgo.domain.search.dto.sub.SpotResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCreateRequest;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseUtil {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    /**
     * HttpServletRequest에서 사용자 ID 추출
     *
     * @param servletRequest - HttpServletRequest 객체
     * @return 사용자 ID
     */
    public Long getUserIdFromRequest(HttpServletRequest servletRequest) {
        return tokenUtil.getUserIdFromHeader(servletRequest);
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @param userId - 사용자 ID
     * @return 조회된 사용자 객체
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));
    }

    /**
     * 시작 지점 추출
     *
     * @param createRequest - 유저 코스 생성 요청 데이터
     * @return 시작 지점에 대한 SpotResponse
     */
    public SpotResponse extractStartSpot(UserJejuGudgoCourseCreateRequest createRequest) {
        var startSpotRequest = createRequest.JejuGudgoSpots().get(0);
        return new SpotResponse(
                null,
                startSpotRequest.title(),
                startSpotRequest.orderNumber(),
                startSpotRequest.latitude(),
                startSpotRequest.longitude()
        );
    }

    /**
     * 끝 지점 추출
     *
     * @param createRequest - 유저 코스 생성 요청 데이터
     * @return 끝 지점에 대한 SpotResponse
     */
    public SpotResponse extractEndSpot(UserJejuGudgoCourseCreateRequest createRequest) {
        var endSpotRequest = createRequest.JejuGudgoSpots().get(createRequest.JejuGudgoSpots().size() - 1);
        return new SpotResponse(
                null,
                endSpotRequest.title(),
                endSpotRequest.orderNumber(),
                endSpotRequest.latitude(),
                endSpotRequest.longitude()
        );
    }
}
