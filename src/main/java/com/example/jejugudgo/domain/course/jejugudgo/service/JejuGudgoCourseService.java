package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.*;
import com.example.jejugudgo.domain.course.jejugudgo.message.JejuGudgoPublisher;
import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.review.util.StarAvgCalculator;
import com.example.jejugudgo.domain.search.dto.sub.JeujuGudgoCourseInfoResponse;
import com.example.jejugudgo.domain.search.dto.sub.SpotResponse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseSpotCreateRequest;
import com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request.UserJejuGudgoCourseTagCreateRequest;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseService {
    private final JejuGudgoPublisher publisher;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final StarAvgCalculator starAvgCalculator;

    private final JejuGudgoCourseSpotService jejuGudgoCourseSpotService;
    private final JejuGudgoCourseTagService jejuGudgoCourseTagService;
    private final JejuGudgoCourseDocumentService jejuGudgoCourseDocumentService;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final UserJejuGudgoCourseSpotRepository userJejuGudgoCourseSpotRepository;
    private final UserJejuGudgoCourseTagRepository userJejuGudgoCourseTagRepository;

    /*
        해당 코드는 유저가 생성한 코스를 완료했을 때 동작합니다.
        관련 메서드는 JejuGudgoCourseSpotService 와 JejuGudgoCourseTagService 에 있습니다.
     */
    public void createJejuGudgoCourseFromUserCourse(JejuGudgoCreateRequest request) {
        JejuGudgoCourse jejuGudgoCourse = JejuGudgoCourse.builder()
                .user(request.userJejuGudgoCourse().getUser())
                .createdAt(request.userJejuGudgoCourse().getCreatedAt())
                .starAvg(0.0)
                .time(request.userJejuGudgoCourse().getTime())
                .distance(request.userJejuGudgoCourse().getDistance())
                .imageUrl(request.userJejuGudgoCourse().getImageUrl())
                .summary(request.userJejuGudgoCourse().getSummary())
                .viewCount(0L)
                .startSpotTitle(request.userJejuGudgoCourse().getStartSpotTitle())
                .startLatitude(request.userJejuGudgoCourse().getStartLatitude())
                .startLongitude(request.userJejuGudgoCourse().getStartLongitude())
                .endSpotTitle(request.userJejuGudgoCourse().getEndSpotTitle())
                .endLatitude(request.userJejuGudgoCourse().getEndLatitude())
                .endLongitude(request.userJejuGudgoCourse().getEndLongitude())
                .isDeleted(false)
                .build();

        jejuGudgoCourseRepository.save(jejuGudgoCourse);

        List<JejuGudgoCourseSpot> spots = jejuGudgoCourseSpotService.createJejuGudgoCourseSpotFromUser(jejuGudgoCourse, request.jejuGudgoCourseSpots());
        List<JejuGudgoCourseTag> tags = jejuGudgoCourseTagService.createJejuGudgoCourseTagFromUserCourse(jejuGudgoCourse, request.jejuGudgoCourseTags());

        publisher.createJejuGudgoCourseMessagePublish(jejuGudgoCourse, spots, tags);
    }

    public void updateStarAvg(double newStarAvg, JejuGudgoCourse jejuGudgoCourse) {
        jejuGudgoCourse = jejuGudgoCourse.updateStarAvg(newStarAvg);
        jejuGudgoCourseRepository.save(jejuGudgoCourse);
        publisher.updateJejuGudgoStarAvgMessagePublish(jejuGudgoCourse, newStarAvg);
    }

    // TODO: 코스 수정 -> 제주 코스 도큐먼트 서비스의 수정과 연결
}
