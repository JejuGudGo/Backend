package com.gudgo.jeju.domain.planner.service;


import com.gudgo.jeju.domain.planner.dto.request.course.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.dto.request.course.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.CourseType;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlannerService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerQueryService plannerQueryService;

    private final ValidationUtil validationUtil;


    @Transactional
    public void create(Long userId, @Valid PlannerCreateRequestDto requestDto) {
        // 프론트로부터 전송받은 거리(distance) 값을 도보 시간으로 변환한다.
        // LocalTime walkingTime = getWalkingTime(requestDto);

        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = Course.builder()
                .type(CourseType.USER)
                .title(requestDto.title())
                .createdAt(LocalDate.now())
                .originalCreatorId(user.getId())
                .build();

        // 저장하여 originalCourseId 업데이트
        courseRepository.save(course);

        // 저장된 course 객체의 ID값을 originalCourseId에 설정
        course = course.withOriginalCourseId(course.getId());
        courseRepository.save(course);

        Planner planner = Planner.builder()
                .startAt(LocalDate.now())
                .isDeleted(false)
                .isPrivate(requestDto.isPrivate())
//                .summary()
//                .time()
                .isCompleted(false)
                .user(user)
                .course(course)
                .build();

        plannerRepository.save(planner);
    }

    @Transactional
    public void update(Long plannerId, PlannerUpdateRequestDto requestDto) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        if (requestDto.startAt() != null) {
            planner = planner.withStartAt(requestDto.startAt());

        }

        if (requestDto.isPrivate() != planner.isPrivate()) {
            planner = planner.withIsPrivate(requestDto.isPrivate());

        }

        if (validationUtil.validateStringValue(requestDto.summary())) {
            planner = planner.withSummary(requestDto.summary());

        }

        if (requestDto.time() != null) {
            planner = planner.withTime(requestDto.time());

        }

        if (requestDto.isCompleted() != planner.isCompleted()) {
            planner = planner.withCompleted(requestDto.isCompleted());
        }

        plannerRepository.save(planner);
    }

    @Transactional
    public void delete(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        planner = planner.withIsDeleted(true);

        plannerRepository.save(planner);
    }
}
