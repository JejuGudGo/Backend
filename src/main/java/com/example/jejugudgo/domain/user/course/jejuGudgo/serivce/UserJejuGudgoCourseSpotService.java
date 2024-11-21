package com.example.jejugudgo.domain.user.course.jejuGudgo.serivce;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseService;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCompletedRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseTagRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJejuGudgoCourseSpotService {


    private final UserJejuGudgoCourseSpotRepository userJejuGudgoCourseSpotRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final JejuGudgoCourseService jejuGudgoCourseService;
    private final UserJejuGudgoCourseTagRepository userJejuGudgoCourseTagRepository;

    /**
     * 스팟 완료 처리
     *
     * @param courseId - userJejuGudgoCourseId
     * @param spotId - 완료 처리할 스팟 ID
     * @param servletRequest - HttpServletRequest 객체 (사용자 정보 추출용)
     * @param completedRequest - 완료 요청 데이터
     */
    @Transactional
    public void completedSpot(
            Long courseId,
            Long spotId,
            HttpServletRequest servletRequest,
            UserJejuGudgoCourseCompletedRequest completedRequest) {

        UserJejuGudgoCourse userCourse = userJejuGudgoCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        UserJejuGudgoCourseSpot spot = userJejuGudgoCourseSpotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        UserJejuGudgoCourseSpot newSpot = spot.updateIsCompleted(true);
        userJejuGudgoCourseSpotRepository.save(newSpot);

        // 모든 스팟이 완료되었는지 확인
        boolean allSpotsCompleted = userJejuGudgoCourseSpotRepository
                .findByUserCourseId(courseId)
                .stream()
                .allMatch(UserJejuGudgoCourseSpot::isCompleted);



        if (allSpotsCompleted) {
            // 모든 스팟 완료: 최초 걷기 처리 또는 업데이트 처리
            if (userCourse.getJejuGudgoCourse() == null) {
                handleFirstWalking(userCourse, courseId);
            } else {
                handleSubsequentWalking(userCourse, completedRequest);
            }
        }
    }

    /**
     * 최초 걷기 처리
     *
     * @param userCourse - userJejuGudgoCourse 엔티티
     * @param courseId - userJejuGudgoCourseId
     */
    private void handleFirstWalking(UserJejuGudgoCourse userCourse, Long courseId) {
        // 1. UserJejuGudgoCourseSpot 및 UserJejuGudgoCourseTag 조회
        List<UserJejuGudgoCourseSpot> spots = userJejuGudgoCourseSpotRepository.findByUserCourseId(courseId);
        List<UserJejuGudgoCourseTag> tags = userJejuGudgoCourseTagRepository.findTagsByUserCourseId(courseId);

        // 2. JejuGudgoCreateRequest 생성
        JejuGudgoCreateRequest createRequest = new JejuGudgoCreateRequest(userCourse, spots, tags);

        // 3. JejuGudgoCourse 생성 및 저장
        jejuGudgoCourseService.createJejuGudgoCourseFromUserCourse(createRequest);
    }

    /**
     * 기존 걷기 처리
     *
     * @param userCourse - 유저 제주 굿고 코스 엔티티
     * @param completedRequest - 완료 요청 데이터
     */
    private void handleSubsequentWalking(UserJejuGudgoCourse userCourse, UserJejuGudgoCourseCompletedRequest completedRequest) {
        // 1. UserJejuGudgoCourse 업데이트
        UserJejuGudgoCourse newUserCourse = userCourse.updateCompleted(
                completedRequest.time().toString(),
                completedRequest.restTime(),
                completedRequest.distance() + "km",
                completedRequest.speed(),
                completedRequest.kcal(),
                completedRequest.steps(),
                LocalDate.now(), // 완료 날짜
                calculateAverageSpeed(completedRequest.distance(), completedRequest.time()),
                calculateAveragePace(completedRequest.time(), completedRequest.steps()),
                true
        );

        // 2. 변경 사항 저장
        userJejuGudgoCourseRepository.save(newUserCourse);
    }


    // 평균 속도 계산
    private double calculateAverageSpeed(double distance, LocalTime time) {
        double hours = time.getHour() + time.getMinute() / 60.0;
        return distance / hours; // 평균 속도 (km/h)
    }

    // 평균 페이스 계산
    private double calculateAveragePace(LocalTime time, Long steps) {
        double minutes = time.getHour() * 60 + time.getMinute();
        return steps != 0 ? minutes / steps : 0; // 걸음당 소요 시간 (분/걸음)
    }


}
