package com.gudgo.jeju.domain.planner.validation;

import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseValidator {
    private final CourseRepository courseRepository;

    public void validateOriginalWriter(Long userId, Long courseId) throws IllegalAccessException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityExistsException::new);

        if (!course.getOriginalCreatorId().equals(userId)) {
            throw new IllegalAccessException();
        }
    }
}