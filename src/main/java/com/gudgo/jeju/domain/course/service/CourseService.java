package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.course.CourseCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
    private final UserRepository userRepository;


    @Transactional
    public void newCourse(@Valid CourseCreateRequestDto requestDto, HttpServletRequest request) {
        // 프론트로부터 전송받은 거리(distance) 값을 도보 시간으로 변환한다.
        LocalTime walkingTime = getWalkingTime(requestDto);

        Course course = Course.builder()
                .user(getUser(request))
                .title(requestDto.title())
                .time(walkingTime)
                .summary(requestDto.summary())
                .createdAt(LocalDate.now())
                .originalCreatorId(getUser(request).getId())    // 현재 userId
                .build();

        Course savedCourse = courseRepository.save(course);

        // 저장된 course 객체의 ID값을 originalCoureseId에 설정
        Course updatedCourse = savedCourse.withOriginalCourseId(savedCourse.getId());

        // 다시 저장하여 originalCourseId 업데이트
        courseRepository.save(updatedCourse);
    }

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

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCourseList() {
        List<Course> courseList = courseRepository.findAllByIsDeletedFalse();
        List<CourseResponseDto> originalCourseList = new ArrayList<>();
        findIdEqualsOriginalCourseId(courseList, originalCourseList);
        return originalCourseList;
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCourseListByUser(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<Course> courseList = courseRepository.findByUserIdAndIsDeletedFalse(userId);
        List<CourseResponseDto> originalCourseList = new ArrayList<>();
        findIdEqualsOriginalCourseId(courseList, originalCourseList);
        return originalCourseList;
    }


    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // isDeleted = true 변경
        Course updatedCourse = course.withDeleted();
        courseRepository.save(updatedCourse);
    }


    //    public void updateCourse(Long courseId, UpdateCourseRequestDto updateCourseRequestDto) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
//
//
//        courseRepository.save(course);
//    }


    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        // userid로 User 객체 조회
        return userRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
    }

    private void findIdEqualsOriginalCourseId(List<Course> courseList, List<CourseResponseDto> originalCourseList) {
        for (Course course : courseList) {
            if (Objects.equals(course.getId(), course.getOriginalCourseId())) {
                originalCourseList.add(new CourseResponseDto(
                        course.getId(),
                        course.getTitle(),
                        course.getTime(),
                        course.getStartAt(),
                        course.getCreatedAt(),
                        course.isDeleted(),
                        course.getOriginalCreatorId(),
                        course.getOriginalCourseId(),
                        course.getSummary()
                ));
            }
        }
    }

    private LocalTime getWalkingTime(CourseCreateRequestDto requestDto) {
        int walkingTimeMinutes = requestDto.distance() / 67;

        int hours = walkingTimeMinutes / 60;
        int minutes = walkingTimeMinutes % 60;

        return LocalTime.of(hours, minutes);

    }



}