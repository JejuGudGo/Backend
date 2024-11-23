package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.message.JejuGudgoPublisher;
import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCreateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseService {
    private final JejuGudgoPublisher publisher;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;

    private final JejuGudgoCourseSpotService jejuGudgoCourseSpotService;
    private final JejuGudgoCourseTagService jejuGudgoCourseTagService;

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
