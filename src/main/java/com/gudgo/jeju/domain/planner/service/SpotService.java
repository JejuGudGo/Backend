package com.gudgo.jeju.domain.planner.service;


import com.gudgo.jeju.domain.planner.dto.request.spot.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.dto.request.spot.SpotCreateUsingApiRequest;
import com.gudgo.jeju.domain.planner.dto.request.spot.SpotUpdateRequestDto;
import com.gudgo.jeju.domain.planner.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.entity.Spot;
import com.gudgo.jeju.domain.planner.entity.SpotType;
import com.gudgo.jeju.domain.planner.query.SpotQueryService;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.repository.SpotRepository;
import com.gudgo.jeju.domain.planner.validation.CourseValidator;
import com.gudgo.jeju.domain.planner.validation.SpotValidator;
import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContentInfo;
import com.gudgo.jeju.domain.tourApi.repository.TourApiCategory1Repository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentInfoRepository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentRepository;
import com.gudgo.jeju.global.data.tourAPI.service.TourApiRequestService;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final TourApiCategory1Repository tourApiCategory1Repository;


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
    public void createUserSpot(Long plannerId, @Valid SpotCreateRequestDto requestDto) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        Course course = planner.getCourse();

        TourApiCategory1 tourApiCategory1 = tourApiCategory1Repository.findById(requestDto.category())
                .orElseThrow(EntityNotFoundException::new);

        Long lastSpotId = spotQueryService.getLastSpotId(course.getId());
        Long orderNumber = (lastSpotId != null) ? lastSpotId + 1L : 1L;

        Spot spot = Spot.builder()
                .course(course)
                .spotType(SpotType.USER)
                .tourApiCategory1(tourApiCategory1)
                .orderNumber(orderNumber)
                .title(requestDto.title())
                .address(requestDto.address())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .isDeleted(false)
                .isCompleted(false)
                .count(0L)
                .contentId("None")
                .build();

        spotRepository.save(spot);
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
}
