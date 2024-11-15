package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseTagService {
    private final JejuGudgoCourseTagRepository jejuGudgoCourseTagRepository;

    public List<JejuGudgoCourseTag> createJejuGudgoCourseTagFromUserCourse(JejuGudgoCourse jejuGudgoCourse, List<UserJejuGudgoCourseTag> userCourseTags) {
        List<JejuGudgoCourseTag> tags = new ArrayList<>();

        for (UserJejuGudgoCourseTag userCourseTag : userCourseTags) {
            JejuGudgoCourseTag jejuGudgoCourseTag = JejuGudgoCourseTag.builder()
                    .courseTag(userCourseTag.getCourseTag())
                    .jejuGudgoCourse(jejuGudgoCourse)
                    .build();

            jejuGudgoCourseTagRepository.save(jejuGudgoCourseTag);
            tags.add(jejuGudgoCourseTag);
        }

        return tags;
    }
}
