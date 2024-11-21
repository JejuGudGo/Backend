package com.example.jejugudgo.domain.course.olle.service;

import com.example.jejugudgo.domain.course.olle.message.JejuOlleCoursePublisher;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JejuOlleCourseService {
    private final JejuOlleCourseRepository jejuOlleCourseRepository;
    private final JejuOlleCoursePublisher publisher;

    public void updateStarAvg(double newStarAvg, JejuOlleCourse olleCourse) {
        olleCourse = olleCourse.updateStarAvg(newStarAvg);
        jejuOlleCourseRepository.save(olleCourse);
        publisher.updateJejuOlleStarAvgMessagePublish(olleCourse, newStarAvg);
    }
}
