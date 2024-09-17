package com.gudgo.jeju.domain.planner.course.service;


import com.gudgo.jeju.domain.planner.course.dto.request.CourseCreateRequestDto;
import com.gudgo.jeju.domain.planner.course.dto.request.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.repository.ReviewRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;


@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final SpotRepository spotRepository;
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

//    public CourseResponseDto getCourse(Long courseId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new EntityNotFoundException("course not found id=" + courseId));
//
//        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderNumberAsc(courseId);
//
//        return new CourseResponseDto(
//                course.getId(),
//                course.getType(),
//                course.getTitle(),
//                course.getCreatedAt(),
//                course.getOriginalCreatorId(),
//                course.getOriginalCourseId(),
//                null,
//                spots
//        );
//    }

//    @Transactional
//    public void createByUserCourse(Long userId, Long courseId, CourseCreateByOtherCourseRequestDto request) {
//         Course originalCourse = courseRepository.findById(courseId)
//                .orElseThrow(EntityNotFoundException::new);
//
//         Long CourseCreator = plannerRepository.findById(courseId).get().getUser().getId();
//
//         User user = userRepository.findById(userId)
//                 .orElseThrow(EntityNotFoundException::new);
//
//
//        Course newCourse = Course.builder()
//                .title(request.title())
//                .createdAt(LocalDate.now())
//                .originalCourseId(originalCourse.getId())
//                .originalCreatorId(CourseCreator)
//                .type(CourseType.USER)
//                .build();
//
//        courseRepository.save(newCourse);
//
//        Planner planner = Planner.builder()
//                .user(user)
//                .course(newCourse)
//                .startAt(LocalDate.now())
//                .isDeleted(false)
//                .isPrivate(true)
////                .summary()
////                .time()
//                .isCompleted(false)
//                .build();
//
//        plannerRepository.save(planner);
//    }
//
//    @Transactional
//    public void createCourseByOlleCourse(Long userId, Long courseId, CourseCreateByOtherCourseRequestDto requestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(courseId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Course course = Course.builder()
//                .title(jeJuOlleCourse.getTitle())
//                .createdAt(LocalDate.now())
//                .type(CourseType.JEJU)
//                .olleCourseId(courseId)
//                .build();
//
//        courseRepository.save(course);
//
//
//        Planner planner = Planner.builder()
//                .user(user)
//                .course(course)
//                .startAt(LocalDate.now())
//                .isDeleted(false)
//                .isPrivate(true)
////                .summary()
//                .time(LocalTime.parse(jeJuOlleCourse.getTotalTime()))
//                .isCompleted(false)
//                .build();
//        plannerRepository.save(planner);
//    }


//    @Transactional
//    public void delete(Long courseId) {
//
//        Planner planner = plannerRepository.findByCourseId(courseId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        planner = planner.withIsDeleted(true);
//
//        plannerRepository.save(planner);
//    }

//    @Transactional
//    public void updatePlanStartAt(Long courseId, CourseUpdateRequestDto requestDto) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new EntityNotFoundException("plan not found with id: " + courseId));
//
//        course = course.withStartAt(requestDto.startAt());
//
//        courseRepository.save(course);
//    }
//


    //    public void updateCourse(Long courseId, UpdateCourseRequestDto updateCourseRequestDto) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
//
//
//        courseRepository.save(course);
//    }


//    private User getUser(HttpServletRequest request) {
//        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
//        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출
//
//        // userid로 User 객체 조회
//        return userRepository.findById(userid)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
//    }

//    private void findIdEqualsOriginalCourseId(List<Course> courseList, List<CourseResponseDto> originalCourseList) {
//        for (Course course : courseList) {
//            if (Objects.equals(course.getId(), course.getOriginalCourseId())) {
//                originalCourseList.add(new CourseResponseDto(
//                        course.getId(),
//                        course.getTitle(),
//                        course.getTime(),
//                        course.getStartAt(),
//                        course.getCreatedAt(),
//                        course.isDeleted(),
//                        course.getOriginalCreatorId(),
//                        course.getOriginalCourseId(),
//                        course.getSummary()
//                ));
//            }
//        }
//    }
}