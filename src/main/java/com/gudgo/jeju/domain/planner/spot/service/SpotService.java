package com.gudgo.jeju.domain.planner.spot.service;


import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import com.gudgo.jeju.domain.badge.event.BadgeEvent;
import com.gudgo.jeju.domain.badge.repository.BadgeRepository;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.service.CourseService;
import com.gudgo.jeju.domain.planner.planner.query.PlannerSearchQueryService;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateUsingApiRequest;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotUpdateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotCreateResponse;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.planner.spot.entity.SpotType;
import com.gudgo.jeju.domain.planner.spot.query.SpotQueryService;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.planner.course.validation.CourseValidator;
import com.gudgo.jeju.domain.planner.spot.validator.SpotValidator;
import com.gudgo.jeju.domain.post.participant.entity.Participant;
import com.gudgo.jeju.domain.post.participant.repository.ParticipantRepository;
import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.domain.tourApi.repository.TourApiCategory1Repository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentRepository;
import com.gudgo.jeju.global.data.tourAPI.service.TourApiRequestService;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotQueryService spotQueryService;
    private final TourApiRequestService requestService;

    private final SpotValidator spotValidator;
    private final CourseValidator courseValidator;

    private final ValidationUtil validationUtil;

    private final SpotRepository spotRepository;
    private final CourseRepository courseRepository;
    private final TourApiContentRepository tourApiContentRepository;
    private final PlannerRepository plannerRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final PlannerSearchQueryService plannerSearchQueryService;
    private final BadgeRepository badgeRepository;


    @Transactional
    public List<SpotResponseDto> getSpots(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderNumberAsc(planner.getCourse().getId());

        return spots.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SpotResponseDto getSpot(Long spotId) {
        Spot spot = findSpotById(spotId);
        return convertToDto(spot);
    }

    @Transactional
    public List<SpotCreateResponse> createUserSpot(Course course, @Valid List<SpotCreateRequestDto> requests) {
        List<SpotCreateResponse> responses = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            String contentType = "None";

            if (requests.get(i).type().equals(SpotType.TOUR)) contentType = requests.get(i).contentTypeId();

            Spot spot = Spot.builder()
                    .spotType(requests.get(i).type())
                    .orderNumber(Long.parseLong(String.valueOf(i)))
                    .title(requests.get(i).title())
                    .address(requests.get(i).address())
                    .latitude(requests.get(i).latitude())
                    .longitude(requests.get(i).longitude())
                    .isCompleted(false)
                    .isDeleted(false)
                    .contentId(contentType)
                    .count(0L)
                    .course(course)
                    .build();

            spotRepository.save(spot);

            responses.add(new SpotCreateResponse(spot.getId(), spot.getTitle(), spot.getLatitude(), spot.getLongitude()));
        }

        String startSpotTitle = responses.get(0).title();
        String lastSpotTitle = responses.get(responses.size() - 1).title();
        String courseContent = startSpotTitle + "부터 " + lastSpotTitle  + "까지 걷는 코스에요.";

        course = course.withContent(courseContent);
        courseRepository.save(course);

        return responses;
    }

    @Transactional
    public void createSpotUsingTourApi(Long plannerId, SpotCreateUsingApiRequest request) throws IOException {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = planner.getCourse();

        TourApiContent tourApiContent = tourApiContentRepository.findById(request.contentId())
                .orElseThrow(EntityNotFoundException::new);

        if (tourApiContent.getTourApiContentInfo() == null) {
            requestService.requestSpotDetail(
                    tourApiContent.getId(),
                    tourApiContent.getTourApiCategory3().getTourApiCategory2().getTourApiCategory1().getTourApiContentType().getId()
            );
            tourApiContent = tourApiContentRepository.findById(tourApiContent.getId()).get(); // 업데이트된 tourApiContent 가져오기
        }

        spotValidator.validateIsCurrentData(tourApiContent);

        Long lastSpotId = spotQueryService.getLastSpotId(course.getId());
        Long orderNumber = (lastSpotId != null) ? lastSpotId + 1L : 1L;

        Spot spot = Spot.builder()
                .course(course)
                .spotType(SpotType.TOUR)
                .tourApiCategory1(tourApiContent.getTourApiCategory3().getTourApiCategory2().getTourApiCategory1())
                .orderNumber(orderNumber)
                .title(tourApiContent.getTourApiContentInfo().getTitle())
                .address(tourApiContent.getTourApiContentInfo().getAddress())
                .latitude(tourApiContent.getLatitude())
                .longitude(tourApiContent.getLongitude())
                .count(0L)
                .isDeleted(false)
                .isCompleted(false)
                .contentId(request.contentId())
                .build();

        spotRepository.save(spot);
    }

    @Transactional
    public void delete(Long userId, Long plannerId, Long spotId) throws IllegalAccessException {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        courseValidator.validateOriginalWriter(userId, planner.getCourse().getId());

        Spot spot = findSpotById(spotId);
        spot = spot.withDeleted();

        spotRepository.save(spot);
    }

    @Transactional
    public void updateOrder(List<SpotUpdateRequestDto> requestDtos) {
        for (SpotUpdateRequestDto requestDto : requestDtos) {
            Spot spot = findSpotById(requestDto.spotId());

            if (validationUtil.validateLongValue(requestDto.spotId())) {
                spot = spot.withOrderNumber(requestDto.orderNumber());

                spotRepository.save(spot);
            }
        }
    }

    @Transactional
    public void completedSpot(Long courseId, Long spotId) {
        Long lastSpotId = spotQueryService.getLastSpotId(courseId);

        // 마지막 스팟일 경우, 걷기 계획 완료 처리
        if (lastSpotId.equals(spotId)) {

            Planner planner = plannerRepository.findByCourseId(courseId)
                    .orElseThrow(EntityNotFoundException::new);
            planner = planner.withCompleted(true);

            plannerRepository.save(planner);

            // 걷기 계획 완료 시, 뱃지 이벤트 발생


            // 1) 올레 코스 or 유저 코스 이용 시 뱃지 부여
            CourseType type = planner.getCourse().getType();
            if (type == CourseType.JEJU || type == CourseType.HAYOUNG) {
                // 올레 코스 이용
                olleCourseBadge(planner);
            } else {
                // 유저 생성 코스 이용
                jejuCourseBadge(planner);
            }

            // 2) 동행자와 걷기 시 뱃지 부여
            participantBadge(planner.getUser().getId());

            // 3) 연속 걷기 뱃지 부여
            consecutiveWalkingBadge(planner.getUser().getId());
        }

        Spot spot = findSpotById(spotId);
        spot = spot.withCompleted();

        spotRepository.save(spot);
    }

    @Transactional
    public void increaseCount(Long spotId) {
        Spot spot = findSpotById(spotId);
        spot = spot.withIncreasedCount();

        spotRepository.save(spot);
    }

    private Spot findSpotById(Long id) {
        return spotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Spot not found with id: " + id));
    }

    private SpotResponseDto convertToDto(Spot spot) {
        return new SpotResponseDto(
                spot.getId(),
                spot.getContentId(),
                spot.getCourse().getId(),
                spot.getTitle(),
                spot.getOrderNumber(),
                spot.getAddress(),
                spot.getLatitude(),
                spot.getLongitude(),
                spot.isCompleted(),
                spot.getCount()
        );
    }

    private void olleCourseBadge(Planner planner) {
        int ollePlannerCount = plannerSearchQueryService.getOllePlannersCount(planner.getUser().getId());
        Long userId = planner.getUser().getId();

        if (ollePlannerCount == 1 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B02)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B02));
        } else if (ollePlannerCount == 3 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B03)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B03));
        } else if (ollePlannerCount == 5 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B04)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B04));
        } else if (ollePlannerCount == 7 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B05)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B05));
        } else if (ollePlannerCount == 10 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B06)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B06));
        }
    }

    private void jejuCourseBadge(Planner planner) {
        int userPlannerCount = plannerSearchQueryService.getUserPlannersCount(planner.getUser().getId());
        Long userId = planner.getUser().getId();

        if (userPlannerCount == 1 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B07)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B07));
        } else if (userPlannerCount == 3 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B08)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B08));
        } else if (userPlannerCount == 5 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B09)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B09));
        } else if (userPlannerCount == 7 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B10)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B10));
        } else if (userPlannerCount == 10 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B11)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B11));
        }
    }

    private void participantBadge(Long userId) {
        int participantCount = plannerSearchQueryService.getParticipateCount(userId);

        if (participantCount == 1 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B17)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B17));
        } else if (participantCount == 3 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B18)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B18));
        } else if (participantCount == 5 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B19)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B19));
        } else if (participantCount == 7 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B20)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B20));
        } else if (participantCount == 10 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B21)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B21));
        }
    }


    private void consecutiveWalkingBadge(Long userId) {
        List<LocalDate> completedWalkDates = plannerSearchQueryService.getStartAt(userId);

        int consecutiveDays = 1;
        LocalDate previousDate = completedWalkDates.get(completedWalkDates.size() - 1);

        for (int i = completedWalkDates.size() - 2; i >= 0; i--) {
            LocalDate currentDate = completedWalkDates.get(i);
            if (currentDate.plusDays(1).equals(previousDate)) {
                consecutiveDays++;
                previousDate = currentDate;
            } else {
                break; // 연속되지 않으면 반복 중단
            }
        }


        if (consecutiveDays == 2 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B22)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B22));
        } else if (consecutiveDays == 4 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B23)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B23));
        } else if (consecutiveDays == 7 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B24)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B24));
        } else if (consecutiveDays == 14 && !badgeRepository.existsByUserIdAndBadgeCode(userId, BadgeCode.B25)) {
            eventPublisher.publishEvent(new BadgeEvent(userId, BadgeCode.B25));
        }
    }
}
