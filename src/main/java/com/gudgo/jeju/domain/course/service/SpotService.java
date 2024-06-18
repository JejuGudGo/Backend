package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.Spot;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import com.gudgo.jeju.domain.course.repository.SpotRepository;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiCategory1;
import com.gudgo.jeju.global.data.tourAPI.common.repository.TourApiCategory1Repository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final CourseRepository courseRepository;
    private final TourApiCategory1Repository tourApiCategory1Repository;


    @Transactional
    public void newSpot(@Valid SpotCreateRequestDto requestDto) {

        Course course = courseRepository.findById(requestDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + requestDto.courseId()));

        TourApiCategory1 tourApiCategory1 = tourApiCategory1Repository.findById(requestDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("TourApiCategory1 not found with id: " + requestDto.categoryId()));

        Spot spot = Spot.builder()
                .course(course)
                .tourApiCategory1(tourApiCategory1)
                .title(requestDto.title())
                .courseType(requestDto.courseType())
                .order(requestDto.order())
                .address(requestDto.address())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
//                .count(0)
                .build();
        spotRepository.save(spot);
    }

    @Transactional
    public List<SpotResponseDto> getSpotsByCourseId(Long courseId) {
        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderAsc(courseId);
        return spots.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public SpotResponseDto getSpot(Long id) {
        Spot spot = findSpotById(id);
        return convertToDto(spot);
    }

    @Transactional
    public void completedSpot(Long id) {
        Spot spot = findSpotById(id);
        spot.updateIsCompleted();
    }

    @Transactional
    public void increaseCount(Long id) {
        Spot spot = findSpotById(id);
        spot.updateCount();
    }

    @Transactional
    public void deleteSpot(Long id) {
        Spot spot = findSpotById(id);
        spot.softDelete();
    }

    private Spot findSpotById(Long id) {
        return spotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Spot not found with id: " + id));
    }

    private SpotResponseDto convertToDto(Spot spot) {
        return new SpotResponseDto(
                spot.getId(),
                spot.getOrder(),
                spot.getAddress(),
                spot.getTitle(),
                spot.getCount(),
                spot.getTourApiCategory1() != null ? spot.getTourApiCategory1().getId() : null,
                spot.getLongitude(),
                spot.getLatitude(),
                spot.isDeleted(),
                spot.isCompleted(),
                spot.getCourse() != null ? spot.getCourse().getId() : null
        );
    }
}
