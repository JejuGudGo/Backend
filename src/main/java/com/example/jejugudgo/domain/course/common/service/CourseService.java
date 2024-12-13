package com.example.jejugudgo.domain.course.common.service;

import com.example.jejugudgo.domain.course.common.dto.response.CourseListResponse;
import com.example.jejugudgo.domain.course.common.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import com.example.jejugudgo.domain.course.common.enums.OlleTag;
import com.example.jejugudgo.domain.course.common.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseTagRepository;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.mygudgo.course.enums.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CourseService {

    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final OlleCourseRepository olleCourseRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final UserJejuGudgoCourseTagRepository userJejuGudgoCourseTagRepository;
    private final OlleCourseTagRepository olleCourseTagRepository;

    public CourseListResponse getCourseForList(Long targetId) {
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(targetId).orElse(null);

        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCourseRepository.findById(targetId).orElse(null);

        if (jejuGudgoCourse != null && userJejuGudgoCourse != null) {
            List<String> tags = getTagsForUserCourse(userJejuGudgoCourse);
            return new CourseListResponse(
                    jejuGudgoCourse.getId(),
                    tags,
                    userJejuGudgoCourse.getTitle(),
                    userJejuGudgoCourse.getRoute(),
                    userJejuGudgoCourse.getDistance(),
                    userJejuGudgoCourse.getTime(),
                    userJejuGudgoCourse.getThumbnailUrl(),
                    jejuGudgoCourse.getStarAvg(),
                    jejuGudgoCourse.getReviewCount()
            );
        }

        // OlleCourse 데이터 조회
        OlleCourse olleCourse = olleCourseRepository.findById(targetId).orElse(null);
        if (olleCourse != null) {
            List<String> tags = getTagsForOlleCourse(olleCourse);
            return new CourseListResponse(
                    olleCourse.getId(),
                    tags,
                    olleCourse.getTitle(),
                    olleCourse.getRoute(),
                    olleCourse.getDistance(),
                    olleCourse.getTime(),
                    olleCourse.getThumbnailUrl(),
                    olleCourse.getStarAvg(),
                    olleCourse.getReviewCount()
            );
        }

        return null;
    }


    private List<String> getTagsForUserCourse(UserJejuGudgoCourse userJejuGudgoCourse) {
        return userJejuGudgoCourseTagRepository.findByUserCourse(userJejuGudgoCourse).stream()
                .map(UserJejuGudgoCourseTag::getTitle)
                .map(JejuGudgoCourseTag::getTag)
                .collect(Collectors.toList());
    }


    private List<String> getTagsForOlleCourse(OlleCourse olleCourse) {
        return olleCourseTagRepository.findByOlleCourse(olleCourse).stream()
                .map(OlleCourseTag::getTitle)
                .map(OlleTag::getTag)
                .collect(Collectors.toList());
    }
}
