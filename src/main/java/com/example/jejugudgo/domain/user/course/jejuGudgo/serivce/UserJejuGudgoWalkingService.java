package com.example.jejugudgo.domain.user.course.jejuGudgo.serivce;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseOptionCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.util.CourseUtil;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.reseponse.UserJejuGudgoWalkingStartResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserJejuGudgoWalkingService {

    private final CourseUtil courseUtil;
    private final UserJejuGudgoCopyService userJejuGudgoCopyService;

    /**
     * 걷기 시작
     *
     * @param courseId - 코스 ID
     * @param servletRequest - HttpServletRequest 객체 (사용자 정보 추출)
     * @param createRequest - 걷기 옵션 요청 데이터
     */
    @Transactional
    public UserJejuGudgoWalkingStartResponse startWalking(Long courseId, HttpServletRequest servletRequest, JejuGudgoCourseOptionCreateRequest createRequest) {
        // 사용자 ID 추출
        Long userId = courseUtil.getUserIdFromRequest(servletRequest);
        // 사용자 정보 확인
        User user = courseUtil.findUserById(userId);

        // UserJejuGudgoCourse 생성
        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCopyService.createUserCourse(courseId, user);
        // 걷기 옵션 업데이트
        userJejuGudgoCopyService.updateWalkingOptions(createRequest, userJejuGudgoCourse);

        return new UserJejuGudgoWalkingStartResponse(
                userJejuGudgoCourse.getId()
        );
    }
}
