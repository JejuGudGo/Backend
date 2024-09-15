package com.gudgo.jeju.domain.planner.planner.service;


import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerCreateResponse;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotCreateResponse;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerTag;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;
import com.gudgo.jeju.domain.planner.tag.repository.PlannerTagRepository;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.planner.dto.request.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
//import com.gudgo.jeju.domain.planner.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.data.label.repository.LabelRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PlannerRepository plannerRepository;
    private final ParticipantRepository participantRepository;

    private final ValidationUtil validationUtil;
    private final PlannerTagRepository plannerTagRepository;
    private final LabelRepository labelRepository;


    @Transactional
    public PlannerCreateResponse create(Long userId, @Valid PlannerCreateRequestDto requestDto, Course course, List<SpotCreateResponse> spots) {
        User user = findUser(userId);
        Planner planner = createAndSavePlanner(user, course, requestDto);
        List<PlannerType> tags = createAndSaveLabel(planner, requestDto.tags());
        createAndSaveParticipant(user, planner);

        return new PlannerCreateResponse(course.getTitle(), tags, spots);
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

    private Planner createAndSavePlanner(User user, Course course, PlannerCreateRequestDto requestDto) {
        Planner planner = Planner.builder()
//                .startAt(LocalDate.now())
                .isDeleted(false)
                .isPrivate(requestDto.isPrivate())
//                .summary(requestDto.summary())
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

    private List<PlannerType> createAndSaveLabel(Planner planner, List<PlannerType> tags) {
        List<PlannerType> responses = new ArrayList<>();

        for (PlannerType code :tags) {
            PlannerTag plannerTag = PlannerTag.builder()
                    .code(code)
                    .planner(planner)
                    .build();

            plannerTagRepository.save(plannerTag);

            responses.add(code);
        }
        return responses;
    }
}
