package com.gudgo.jeju.domain.planner.course.service;


import com.gudgo.jeju.domain.planner.course.dto.request.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.review.entity.PlannerReview;
import com.gudgo.jeju.domain.planner.review.repository.PlannerReviewRepository;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final SpotRepository spotRepository;
    private final PlannerRepository plannerRepository;

    private final ValidationUtil validationUtil;
    private final PlannerReviewRepository plannerReviewRepository;

    @Transactional
    public void update(Long courseId, CourseUpdateRequestDto requestDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        // 코스명(title) 업데이트
        if (validationUtil.validateStringValue(requestDto.title())) {
            course = course.withTitle(requestDto.title());
        }

        // 대표이미지(imageUrl) 업데이트
        if (validationUtil.validateStringValue(requestDto.imageUrl())) {
            course = course.withImageUrl(requestDto.imageUrl());
        }

        // 코스정보(content) 업데이트
        if (validationUtil.validateStringValue(requestDto.content())) {
            course = course.withContent(requestDto.content());
        }

        courseRepository.save(course);
    }

    @Transactional
    public void updateAllOriginalCourseStarAvg() {

        List<Course> originalCourses = courseRepository.findOriginalCourses();
        for (Course course : originalCourses) {
            List<PlannerReview> reviews = plannerReviewRepository.findByPlannerCourseOriginalCourseIdAndIsDeletedFalse(course.getId());
            OptionalDouble avgStars = reviews.stream()
                    .mapToLong(PlannerReview::getStars)
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
//    public void create(Long userId, @Valid PlannerCreateRequestDto requestDto) {
////        // 프론트로부터 전송받은 거리(distance) 값을 도보 시간으로 변환한다.
////        LocalTime walkingTime = getWalkingTime(requestDto);
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Course course = Course.builder()
//                .type(CourseType.USER)
//                .title(requestDto.title())
//                .createdAt(LocalDate.now())
//                .originalCreatorId(user.getId())
//                .build();
//
//        // 저장된 course 객체의 ID값을 originalCoureseId에 설정
//        course = course.withOriginalCourseId(course.getId());
//
//        // 저장하여 originalCourseId 업데이트
//        courseRepository.save(course);
//
//        Planner planner = Planner.builder()
//                .startAt(LocalDate.now())
//                .isDeleted(false)
//                .isPrivate(requestDto.isPrivate())
////                .summary()
////                .time()
//                .isCompleted(false)
//                .user(user)
//                .course(course)
//                .build();
//
//        plannerRepository.save(planner);
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