package com.gudgo.jeju.domain.planner.spot.service;


import com.gudgo.jeju.domain.planner.course.service.CourseService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    private final CourseService courseService;
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
