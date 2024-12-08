package com.example.jejugudgo.domain.course.common.query;

import com.example.jejugudgo.domain.course.common.dto.response.CourseListResponse;
import com.example.jejugudgo.domain.course.common.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import com.example.jejugudgo.domain.course.common.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseTagRepository;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CourseQueryService {

    // 데이터베이스 레포지토리
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository; // 제주객의 길 데이터 접근
    private final OlleCourseRepository olleCourseRepository; // 올레길 데이터 접근
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository; // 사용자 정의 제주객의 길 데이터 접근
    private final UserJejuGudgoCourseTagRepository userJejuGudgoCourseTagRepository; // 사용자 정의 제주객의 길 태그 접근
    private final OlleCourseTagRepository olleCourseTagRepository; // 올레길 태그 데이터 접근

    /**
     * 특정 코스 ID를 기반으로 CourseListResponse를 반환하는 메서드
     *
     * @param targetId 코스 ID
     * @return CourseListResponse
     */
    public CourseListResponse getCourseForList(Long targetId) {
        // JejuGudgoCourse 데이터 조회
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(targetId).orElse(null);

        // UserJejuGudgoCourse 데이터 조회
        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCourseRepository.findById(targetId).orElse(null);

        // JejuGudgoCourse와 UserJejuGudgoCourse를 결합하여 응답 생성
        if (jejuGudgoCourse != null && userJejuGudgoCourse != null) {
            List<String> tags = getTagsForUserCourse(userJejuGudgoCourse); // 태그 조회
            return new CourseListResponse(
                    jejuGudgoCourse.getId(),
                    tags, // 태그 리스트
                    userJejuGudgoCourse.getTitle(), // 사용자 정의 제목
                    userJejuGudgoCourse.getRoute(), // 사용자 정의 경로
                    userJejuGudgoCourse.getDistance(), // 사용자 정의 거리
                    userJejuGudgoCourse.getTime(), // 사용자 정의 시간
                    userJejuGudgoCourse.getThumbnailUrl(), // 사용자 정의 썸네일
                    jejuGudgoCourse.getStarAvg(), // JejuGudgoCourse 평균 별점
                    jejuGudgoCourse.getReviewCount() // JejuGudgoCourse 리뷰 수
            );
        }

        // OlleCourse 데이터 조회
        OlleCourse olleCourse = olleCourseRepository.findById(targetId).orElse(null);
        if (olleCourse != null) {
            List<String> tags = getTagsForOlleCourse(olleCourse); // 태그 조회
            return new CourseListResponse(
                    olleCourse.getId(),
                    tags, // 태그 리스트
                    olleCourse.getTitle(),
                    olleCourse.getRoute(),
                    olleCourse.getDistance(),
                    olleCourse.getTime(),
                    olleCourse.getThumbnailUrl(),
                    olleCourse.getStarAvg(),
                    olleCourse.getReviewCount()
            );
        }

        return null; // 조회된 데이터가 없으면 null 반환
    }

    /**
     * UserJejuGudgoCourse와 연결된 태그 리스트를 반환
     *
     * @param userJejuGudgoCourse 사용자 정의 제주객의 길 엔티티
     * @return 태그 리스트
     */
    private List<String> getTagsForUserCourse(UserJejuGudgoCourse userJejuGudgoCourse) {
        return userJejuGudgoCourseTagRepository.findByUserCourse(userJejuGudgoCourse).stream()
                .map(UserJejuGudgoCourseTag::getTitle) // Enum 추출
                .map(Enum::name) // Enum 값을 문자열로 변환
                .collect(Collectors.toList());
    }

    /**
     * OlleCourse와 연결된 태그 리스트를 반환
     *
     * @param olleCourse 올레길 엔티티
     * @return 태그 리스트
     */
    private List<String> getTagsForOlleCourse(OlleCourse olleCourse) {
        return olleCourseTagRepository.findByOlleCourse(olleCourse).stream()
                .map(OlleCourseTag::getTitle) // Enum 추출
                .map(Enum::name) // Enum 값을 문자열로 변환
                .collect(Collectors.toList());
    }
}
