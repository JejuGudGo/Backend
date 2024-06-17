package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.course.entity.Spot;
import com.gudgo.jeju.domain.course.repository.SpotRepository;
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

    @Transactional
    public void newSpot(@Valid SpotCreateRequestDto requestDto) {
        Spot spot = Spot.builder()
//                .course()
//                .tourApiCategory1()
                .title(requestDto.title())
//                .courseType()
                .order(requestDto.order())
                .address(requestDto.address())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
//                .count()
                .build();
    }

    public List<SpotResponseDto> getSpotsByCourseId(Long courseId) {
        List<Spot> spots = spotRepository.findByCourseIdOrderByOrderAsc(courseId);

        return spots.stream()
                .map(spot -> new SpotResponseDto(
                        spot.getId(),
                        spot.getOrder(),
                        spot.getAddress(),
                        spot.getTitle(),
                        spot.getCount(),
                        spot.getTourApiCategory1().getId(),
                        spot.getLongitude(),
                        spot.getLatitude(),
                        spot.isDeleted(),
                        spot.isCompleted(),
                        spot.getCourse().getId()
                ))
                .collect(Collectors.toList());

    }

    public SpotResponseDto getSpot(Long id) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Spot not found"));
        return new SpotResponseDto(
                spot.getId(),
                spot.getOrder(),
                spot.getAddress(),
                spot.getTitle(),
                spot.getCount(),
                spot.getTourApiCategory1().getId(),
                spot.getLongitude(),
                spot.getLatitude(),
                spot.isDeleted(),
                spot.isCompleted(),
                spot.getCourse().getId()
        );
    }

}
