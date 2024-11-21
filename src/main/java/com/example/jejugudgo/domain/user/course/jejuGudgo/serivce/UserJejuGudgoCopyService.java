package com.example.jejugudgo.domain.user.course.jejuGudgo.serivce;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseOptionCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.entity.*;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseOptionRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseSpotCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseTagCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJejuGudgoCopyService {
    private final JejuGudgoCourseSpotRepository spotRepository;
    private final JejuGudgoCourseTagRepository tagRepository;
    private final JejuGudgoCourseOptionRepository jejuGudgoCourseOptionRepository;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final JejuGudgoCourseSpotRepository jejuGudgoCourseSpotRepository;

    /**
     * 코스 Spots 생성
     *
     * @param spotRequests - 요청으로 들어온 Spot 리스트
     * @param course       - 유저 코스 객체
     * @return 생성된 UserJejuGudgoCourseSpot 리스트
     */
    public List<UserJejuGudgoCourseSpot> createCourseSpots(List<UserJejuGudgoCourseSpotCreateRequest> spotRequests, UserJejuGudgoCourse course) {
        return spotRequests.stream()
                .map(request -> {
                    JejuGudgoCourseSpot jejuGudgoCourseSpot = jejuGudgoCourseSpotRepository.findById(request.jejuGudgoSpotId())
                            .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

                    return UserJejuGudgoCourseSpot.builder()
                            .jejuGudgoCourseSpot(jejuGudgoCourseSpot)
                            .title(request.title())
                            .spotType(SpotType.fromType(request.spotType()))
                            .orderNumber(request.orderNumber())
                            .address(request.address())
                            .latitude(request.latitude())
                            .longitude(request.longitude())
                            .userCourse(course)
                            .build();
                })
                .toList();
    }

    /**
     * 코스 Tags 생성
     *
     * @param tagRequests - 요청으로 들어온 Tag 리스트
     * @param course - 유저 코스 객체
     * @return 생성된 UserJejuGudgoCourseTag 리스트
     */
    public List<UserJejuGudgoCourseTag> createCourseTags(List<UserJejuGudgoCourseTagCreateRequest> tagRequests, UserJejuGudgoCourse course) {
        return tagRequests.stream()
                .map(request -> UserJejuGudgoCourseTag.builder()
                        .courseTag(CourseTag.valueOf(request.tag()))
                        .userJejuGudgoCourse(course)
                        .build())
                .toList();
    }

    /**
     * UserJejuGudgoCourse 생성
     *
     * @param courseId - JejuGudgoCourse의 ID
     * @param user - 현재 요청한 사용자
     * @return 새로 생성된 UserJejuGudgoCourse
     */
    public UserJejuGudgoCourse createUserCourse(Long courseId, User user) {
        // JejuGudgoCourse 조회
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

        UserJejuGudgoCourse userJejuGudgoCourse = UserJejuGudgoCourse.builder()
                .title(jejuGudgoCourse.getTitle())
                .content(jejuGudgoCourse.getContent())
                .imageUrl(jejuGudgoCourse.getImageUrl())
                .createdAt(LocalDate.now())
                .jejuGudgoCourse(jejuGudgoCourse) // 기존 코스와 연결
                .user(user) // 사용자와 연결
                .build();
        return userJejuGudgoCourseRepository.save(userJejuGudgoCourse);
    }

    /**
     * 걷기 옵션 업데이트
     *
     * @param createRequest - 걷기 옵션 생성 요청 데이터
     * @param userJejuGudgoCourse - 연결된 UserJejuGudgoCourse 객체
     */
    public void updateWalkingOptions(JejuGudgoCourseOptionCreateRequest createRequest, UserJejuGudgoCourse userJejuGudgoCourse) {
        // 요청에서 WalkingType 추출
        String walkingType = createRequest.jejuGudgoOptions().get(0).walkingType();

        // JejuGudgoCourse에서 걷기 옵션 조회
        JejuGudgoCourse jejuGudgoCourse = userJejuGudgoCourse.getJejuGudgoCourse();
        JejuGudgoCourseOption existingOption = jejuGudgoCourseOptionRepository
                .findByJejuGudgoCourseAndWalkingType(jejuGudgoCourse.getId(), walkingType)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

        // UserJejuGudgoCourseOption 생성 및 저장
        JejuGudgoCourseOption newOption = JejuGudgoCourseOption.builder()
                .walkingType(existingOption.getWalkingType()) // 걷기 타입 설정
                .time(existingOption.getTime()) // 기존 옵션 시간
                .distance(existingOption.getDistance()) // 기존 옵션 거리
                .jejuGudgoCourse(null) // JejuGudgoCourse는 설정하지 않음
                .userJejuGudgoCourse(userJejuGudgoCourse) // UserJejuGudgoCourse와 연결
                .build();

        // 새 걷기 옵션 저장
        jejuGudgoCourseOptionRepository.save(newOption);
    }


}
