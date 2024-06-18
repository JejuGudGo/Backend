package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.PlanCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.PlanUpdateIsCompletedRequestDto;
import com.gudgo.jeju.domain.course.dto.request.PlanUpdateStartRequestDto;
import com.gudgo.jeju.domain.course.dto.response.PlanResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Service
public class PlanService {
    private final CourseRepository courseRepository;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
    private final UserRepository userRepository;


    @Transactional
    public void newPlan(@Valid PlanCreateRequestDto requestDto, HttpServletRequest request) {

        // originalCourseId : client로 부터 받은 원작자 코스 인덱스
        Long originalCourseId = requestDto.originalCourseId();

        // originalCourseId로 Course 객체 조회
        Course originalCourse = courseRepository.findById(originalCourseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + originalCourseId));

        // originalCreatorId : 원작자 코스를 작성한 유저의 userid
        Long originalCreatorId = originalCourse.getUser().getId();

        Course newPlan = Course.builder()
                .user(getUser(request))
                .startAt(requestDto.startAt())
                .title(originalCourse.getTitle())
                .time(requestDto.time().toLocalTime())
                .summary(requestDto.summary())
                .createdAt(LocalDate.now())
                .originalCreatorId(originalCreatorId)
                .originalCourseId(requestDto.originalCourseId())
                .build();

        courseRepository.save(newPlan);
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDto> getPlanListByUser(HttpServletRequest request) {
        Long userId = getUser(request).getId();

        List<Course> planList =  courseRepository.findByUserIdAndIsDeletedFalse(userId);
        List<PlanResponseDto> userPlanList = new ArrayList<>();

        findIdDifferentOriginalCourseId(planList, userPlanList);
        return userPlanList;
    }

    @Transactional(readOnly = true)
    public PlanResponseDto getPlanByCourseId(Long courseId) {
        Course plan = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return new PlanResponseDto(
                plan.getId(),
                plan.getUser().getId(),
                plan.getTitle(),
                plan.getTime(),
                plan.getStartAt(),
                plan.getCreatedAt(),
                plan.isCompleted(),
                plan.isDeleted(),
                plan.getOriginalCreatorId(),
                plan.getOriginalCourseId()

        );
    }

    @Transactional
    public void updatePlanStartAt(Long courseId, PlanUpdateStartRequestDto requestDto) {
        Course plan = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("plan not found with id: " + courseId));
        plan.updateStartAt(requestDto);
    }

    @Transactional
    public void updatePlanIsCompleted(Long courseId) {
        Course plan = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("plan not found with id: " + courseId));

        plan.updateIsCompleted();
    }

    public void deletePlan(Long courseId) {
        Course plan = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("plan not found with id: " + courseId));
        plan.softDelete();
        courseRepository.save(plan);
    }

    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        // userid로 User 객체 조회
        return userRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
    }

    private void findIdDifferentOriginalCourseId(List<Course> planList, List<PlanResponseDto> userPlanList) {
        for (Course plan : planList) {
            if (!Objects.equals(plan.getId(), plan.getOriginalCourseId())) {
                userPlanList.add(new PlanResponseDto(
                        plan.getId(),
                        plan.getUser().getId(),
                        plan.getTitle(),
                        plan.getTime(),
                        plan.getStartAt(),
                        plan.getCreatedAt(),
                        plan.isCompleted(),
                        plan.isDeleted(),
                        plan.getOriginalCreatorId(),
                        plan.getOriginalCourseId()
                ));
            }
        }
    }
}
