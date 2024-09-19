package com.gudgo.jeju.domain.planner.course.service;


import com.gudgo.jeju.domain.planner.course.dto.request.CourseCreateRequestDto;
import com.gudgo.jeju.domain.planner.course.dto.request.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.review.entity.Review;
import com.gudgo.jeju.domain.review.repository.ReviewRepository;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.OptionalDouble;


@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final PlannerRepository plannerRepository;
    private final ImageUpdateService imageUpdateService;

    private final ValidationUtil validationUtil;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Course createCourse(@Valid CourseCreateRequestDto requestDto) {
        Course course = Course.builder()
                .type(requestDto.type())
                .title(requestDto.title())
                .createdAt(LocalDate.now())
                .originalCreatorId(requestDto.userId())
                .totalDistance("None")
                .build();

        courseRepository.save(course);
        updateOrgCourseId(course.getId());

        return course;
    }

    @Transactional
    protected void updateOrgCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        course = course.withOriginalCourseId(course.getId());

        courseRepository.save(course);
    }

    @Transactional
    public void updateCourse(Long userId, Long plannerId, MultipartFile file, CourseUpdateRequestDto requestDto) throws Exception {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = planner.getCourse();

        if (validationUtil.validateStringValue(requestDto.title())) {
            course = course.withTitle(requestDto.title());
        }

        if (file != null && !file.isEmpty()) {
            Path imagePath = imageUpdateService.saveImage(userId, file, Category.COURSE);
            course = course.withImageUrl(imagePath.toString());
        }

        if (validationUtil.validateStringValue(requestDto.content())) {
            course = course.withContent(requestDto.content());
        }

        courseRepository.save(course);
    }

    @Transactional
    public void updateAllOriginalCourseStarAvg() {

        List<Course> originalCourses = courseRepository.findOriginalCourses();
        for (Course course : originalCourses) {
            List<Review> reviews = reviewRepository.findByPlannerCourseOriginalCourseIdAndIsDeletedFalse(course.getId());
            OptionalDouble avgStars = reviews.stream()
                    .mapToDouble(Review::getStars)
                    .average();

            if (avgStars.isPresent()) {
                Course updatedCourse = course.withStarAvg(avgStars.getAsDouble());
                courseRepository.save(updatedCourse);
            }
        }
    }

    @Transactional
    public void updateCourseStartAt(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        Planner planner = plannerRepository.findByCourse(course)
                .orElseThrow(EntityNotFoundException::new);

        course = course.withTimeLabs(LocalTime.now());
        planner = planner.withStartAt(LocalDate.now());

        courseRepository.save(course);
        plannerRepository.save(planner);
    }

    public void calculateTimeLabs(Long courseId, LocalTime endTime) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("plan not found with id: " + courseId));

        LocalTime startTime = course.getTimeLabs();

        Duration duration = Duration.between(startTime, endTime);
        LocalTime calculateTime = LocalTime.MIN.plusHours(duration.toHours()).plusMinutes(duration.toMinutes() % 60);

        course = course.withTimeLabs(calculateTime);
        courseRepository.save(course);
    }
}