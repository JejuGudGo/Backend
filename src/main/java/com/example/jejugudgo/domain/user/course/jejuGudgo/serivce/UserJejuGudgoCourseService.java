package com.example.jejugudgo.domain.user.course.jejuGudgo.serivce;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseOptionCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseOptionResponse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.*;
import com.example.jejugudgo.domain.course.jejugudgo.message.JejuGudgoPublisher;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseOptionRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseDocumentService;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseSpotService;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseTagService;
import com.example.jejugudgo.domain.course.jejugudgo.util.CourseUtil;
import com.example.jejugudgo.domain.review.util.StarAvgCalculator;
import com.example.jejugudgo.domain.search.dto.sub.JeujuGudgoCourseInfoResponse;
import com.example.jejugudgo.domain.search.dto.sub.SpotResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCompletedRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseSpotCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseTagCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.reseponse.UserJejugudgoCourseResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
/**
 * 사용자 코스 관련 주요 로직을 처리하는 서비스.
 * - 코스 생성
 * - 사용자 맞춤 코스 응답 생성
 */
@Service
@RequiredArgsConstructor
public class UserJejuGudgoCourseService {
    private final CourseUtil courseUtil;
    private final UserJejuGudgoCopyService courseCopyService;

    /**
     * 유저 코스 생성
     *
     * @param servletRequest - HttpServletRequest 객체 (헤더에서 사용자 정보 추출)
     * @param createRequest - 유저 코스 생성 요청 데이터
     * @return 생성된 유저 코스 정보
     */
    public JeujuGudgoCourseInfoResponse createUserCourse(HttpServletRequest servletRequest, UserJejuGudgoCourseCreateRequest createRequest) {
        // 요청에서 사용자 ID 추출
        Long userId = courseUtil.getUserIdFromRequest(servletRequest);
        // 사용자 정보 조회
        User user = courseUtil.findUserById(userId);

        // 새로운 유저 코스 생성
        UserJejuGudgoCourse userJejuGudgoCourse = createUserJejuGudgoCourse(createRequest, user);
        // Spots 생성 및 저장
        List<UserJejuGudgoCourseSpot> userJejuGudgoCourseSpots = courseCopyService.createCourseSpots(createRequest.JejuGudgoSpots(), userJejuGudgoCourse);
        // Tags 생성 및 저장
        List<UserJejuGudgoCourseTag> userJejuGudgoCourseTags = courseCopyService.createCourseTags(createRequest.JejuGudgoTags(), userJejuGudgoCourse);

        // 최종 응답 생성
        return buildCourseResponse(userJejuGudgoCourse, userJejuGudgoCourseSpots, userJejuGudgoCourseTags);
    }

    /**
     * 새로운 유저 코스 생성
     *
     * @param createRequest - 유저 코스 생성 요청 데이터
     * @param user - 현재 요청한 사용자
     * @return 생성된 유저 코스 객체
     */
    private UserJejuGudgoCourse createUserJejuGudgoCourse(UserJejuGudgoCourseCreateRequest createRequest, User user) {
        // 시작 및 끝 지점 추출
        SpotResponse startSpot = courseUtil.extractStartSpot(createRequest);
        SpotResponse endSpot = courseUtil.extractEndSpot(createRequest);

        // 유저 코스 빌드 및 저장
        return UserJejuGudgoCourse.builder()
                .title(createRequest.title())
                .isPrivate(true)
                .createdAt(LocalDate.now())
                .summary(startSpot.title() + " - " + endSpot.title()) // 요약 정보
                .content(createRequest.content()) // 코스 설명
                .user(user) // 유저와 연결
                .build();
    }

    /**
     * 최종 응답 생성
     *
     * @param course - 생성된 유저 코스
     * @param spots - 생성된 코스 Spots 리스트
     * @param tags - 생성된 코스 Tags 리스트
     * @return 유저 코스에 대한 상세 응답 객체
     */
    private JeujuGudgoCourseInfoResponse buildCourseResponse(UserJejuGudgoCourse course, List<UserJejuGudgoCourseSpot> spots, List<UserJejuGudgoCourseTag> tags) {
        // SpotResponse 리스트 생성
        List<SpotResponse> spotResponses = spots.stream()
                .map(spot -> new SpotResponse(
                        spot.getId(),
                        spot.getTitle(),
                        spot.getOrderNumber(),
                        spot.getLatitude(),
                        spot.getLongitude()))
                .toList();

        // 시작 및 끝 지점 설정
        SpotResponse startSpot = spotResponses.get(0);
        SpotResponse endSpot = spotResponses.get(spotResponses.size() - 1);

        // 최종 응답 객체 반환
        return new JeujuGudgoCourseInfoResponse(
                spotResponses,
                course.getContent(),
                startSpot,
                endSpot
        );
    }





    // TODO: 유저 코스 완료 처리 -> 제주걷고 생성과 연결
//    public UserJejugudgoCourseResponse completedCourse(Long courseId, HttpServletRequest servletRequest, UserJejuGudgoCourseCompletedRequest completedRequest) {
//
//    }
    // TODO: 유저 코스 수정 -> 제주걷고 코스 수정 연결

    // TODO: 유저 코스 삭제 -> 문의
}
