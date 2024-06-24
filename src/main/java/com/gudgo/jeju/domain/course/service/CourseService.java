package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateByOtherCourseRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.CourseType;
import com.gudgo.jeju.domain.course.query.CourseQueryService;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;

    private final ValidationUtil validationUtil;


    public CourseResponseDto getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("course not found id=" + courseId));

        return new CourseResponseDto(
                course.getId(),
                course.getTitle(),
                course.getTime(),
                course.getStartAt(),
                course.getCreatedAt(),
                course.isDeleted(),
                course.getOriginalCreatorId(),
                course.getOriginalCreatorId(),
                course.getSummary()
        );
    }

    @Transactional
    public void create(Long userId, @Valid CourseCreateRequestDto requestDto) {
//        // 프론트로부터 전송받은 거리(distance) 값을 도보 시간으로 변환한다.
//        LocalTime walkingTime = getWalkingTime(requestDto);
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = Course.builder()
                .user(user)
                .title(requestDto.title())
//                .time(walkingTime)
                .startAt(requestDto.startAt())
//                .summary(requestDto.summary())
                .createdAt(LocalDate.now())
                .originalCreatorId(user.getId())
                .build();

        // 저장된 course 객체의 ID값을 originalCoureseId에 설정
        course = course.withOriginalCourseId(course.getId());

        // 저장하여 originalCourseId 업데이트
        courseRepository.save(course);
    }

    @Transactional
    public void createByUserCourse(Long userId, Long courseId, CourseCreateByOtherCourseRequestDto request) {
         Course originalCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

         User user = userRepository.findById(userId)
                 .orElseThrow(EntityNotFoundException::new);

        Course newCourse = Course.builder()
                .user(user)
                .startAt(request.startAt())
                .title(request.title())
                .createdAt(LocalDate.now())
                .originalCreatorId(originalCourse.getUser().getId())
                .originalCourseId(originalCourse.getId())
                .type(CourseType.USER)
                .build();

        courseRepository.save(newCourse);
    }

    @Transactional
    public void createCourseByOlleCourse(Long userId, Long courseId, CourseCreateByOtherCourseRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = Course.builder()
                .user(user)
                .startAt(requestDto.startAt())
                .title(jeJuOlleCourse.getTitle())
                .createdAt(LocalDate.now())
                .type(CourseType.JEJU)
                .olleCourseId(courseId)
                .build();

        courseRepository.save(course);
    }

    @Transactional
    public void update(Long courseId, CourseUpdateRequestDto requestDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(requestDto.title())) {
            course = course.withTitle(requestDto.title());
        }

        if (requestDto.startAt() != null && !course.isCompleted()) {
            course = course.withStartAt(requestDto.startAt());
        }

        courseRepository.save(course);
    }

    @Transactional
    public void delete(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // isDeleted = true 변경
        Course updatedCourse = course.withDeleted();
        courseRepository.save(updatedCourse);
    }

    @Transactional
    public void updatePlanStartAt(Long courseId, CourseUpdateRequestDto requestDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("plan not found with id: " + courseId));

        course = course.withStartAt(requestDto.startAt());

        courseRepository.save(course);
    }

    @Transactional
    public void updatePlanIsCompleted(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("plan not found with id: " + courseId));

        course = course.withIsCompleted();

        courseRepository.save(course);
    }

//    @Transactional(readOnly = true)
//    public List<CourseResponseDto> getCourseList() {
//        List<Course> courseList = courseRepository.findAllByIsDeletedFalse();
//        List<CourseResponseDto> originalCourseList = new ArrayList<>();
//        findIdEqualsOriginalCourseId(courseList, originalCourseList);
//        return originalCourseList;
//    }


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