package com.gudgo.jeju.domain.planner.planner.service;


import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.label.entity.PlannerLabel;
import com.gudgo.jeju.domain.planner.label.repository.PlannerLabelRespository;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerResponse;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.data.label.dto.response.LabelResponseDto;
import com.gudgo.jeju.global.data.label.entity.Label;
import com.gudgo.jeju.global.data.label.repository.LabelRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Service
@RequiredArgsConstructor
public class PlannerService {

    private static final Logger log = LoggerFactory.getLogger(PlannerService.class);


    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PlannerRepository plannerRepository;
    private final ParticipantRepository participantRepository;

    private final ValidationUtil validationUtil;
    private final PlannerLabelRespository plannerLabelRespository;
    private final LabelRepository labelRepository;


    @Transactional
    public PlannerResponse create(Long userId, @Valid PlannerCreateRequestDto requestDto) {
        User user = findUser(userId);
        Course course = createAndSaveCourse(user, requestDto);
        Planner planner = createAndSavePlanner(user, course, requestDto);
        PlannerLabel plannerLabel = createAndSaveLabel(planner, requestDto.labelRequestDto().code());
        createAndSaveParticipant(user, planner);
        return createUserPlannerResponse(planner, course, plannerLabel);
    }

    @Transactional
    public void createOllePlanner(JeJuOlleCourse olleCourse) {
        CourseType courseType;
        if (olleCourse.getOlleType() == OlleType.JEJU) {
            courseType = CourseType.JEJU;
        } else if (olleCourse.getOlleType() == OlleType.HAYOUNG) {
            courseType = CourseType.HAYOUNG;
        } else {
            throw new IllegalArgumentException("Unsupported OlleType: " + olleCourse.getOlleType());
        }

        Course course = Course.builder()
                .type(courseType)
                .title(olleCourse.getTitle())
                .olleCourseId(olleCourse.getId())
                .build();

        // 저장하여 originalCourseId 업데이트
        courseRepository.save(course);

        // 저장된 course 객체의 ID값을 originalCourseId에 설정
        course = course.withOriginalCourseId(course.getId());
        courseRepository.save(course);

        Planner planner = Planner.builder()
                .isDeleted(false)
                .isPrivate(false)
//                .summary()
//                .time()
                .isCompleted(true)
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

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private Course createAndSaveCourse(User user, PlannerCreateRequestDto requestDto) {
        Course course = Course.builder()
                .type(CourseType.USER)
                .title(requestDto.title())
                .createdAt(LocalDate.now())
                .originalCreatorId(user.getId())
                .build();

        courseRepository.save(course);

        course = course.withOriginalCourseId(course.getId());
        courseRepository.save(course);

        return courseRepository.save(course);
    }

    private Planner createAndSavePlanner(User user, Course course, PlannerCreateRequestDto requestDto) {
        Planner planner = Planner.builder()
//                .startAt(LocalDate.now())
                .isDeleted(false)
                .isPrivate(requestDto.isPrivate())
                .summary(requestDto.summary())
                .isCompleted(false)
                .user(user)
                .course(course)
                .build();

        return plannerRepository.save(planner);
    }

    private void createAndSaveParticipant(User user, Planner planner) {
        Participant participant = Participant.builder()
                .user(user)
                .planner(planner)
                .isDeleted(false)
                .build();

        participantRepository.save(participant);
    }

    private PlannerResponse createUserPlannerResponse(Planner planner, Course course, PlannerLabel plannerLabel) {
        CourseResponseDto courseResponseDto = new CourseResponseDto(
                course.getId(),
                course.getType(),
                course.getTitle(),
                course.getCreatedAt(),
                course.getOriginalCreatorId(),
                course.getOriginalCourseId(),
                null,
                null,
                null,
                course.getStarAvg(),
                null
        );


        return new PlannerResponse(
                planner.getId(),
                planner.getStartAt(),
                planner.getSummary(),
                planner.getTime(),
                planner.isCompleted(),
                plannerLabel.getCode(),
                courseResponseDto
        );
    }

    private PlannerLabel createAndSaveLabel(Planner planner, String code) {
        PlannerLabel plannerLabel = PlannerLabel.builder()
                .code(code)
                .planner(planner)
                .build();

        return plannerLabelRespository.save(plannerLabel);
    }
}
