package com.gudgo.jeju.domain.planner.component;


import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory3;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContentInfo;
import com.gudgo.jeju.domain.tourApi.repository.TourApiCategory3Repository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentInfoRepository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TourApiSpotFinder {

    private final TourApiCategory3Repository tourApiCategory3Repository;
    private final TourApiContentRepository tourApiContentRepository;
    private final TourApiContentInfoRepository tourApiContentInfoRepository;

    @Transactional(readOnly = true)
    public List<TourApiSpotResponseDto> searchTourApiSpots(double latitude, double longitude) {
        final double radiusKm = 1.0;    // 반경 1km


        double latitudeDelta = radiusKm / 111.0;
        double longitudeDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(latitudeDelta)));

        double minLat = latitude - latitudeDelta;
        double maxLat = latitude + latitudeDelta;
        double minLng = longitude - longitudeDelta;
        double maxLng = longitude + longitudeDelta;

        List<TourApiContent> tourApiSpots = tourApiContentRepository
                .findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);

        List<TourApiSpotResponseDto> tourApiSpotDtos = tourApiSpots.stream()
                .filter(spot -> isWithinRadius(spot.getLatitude(), spot.getLongitude(), latitude, longitude, radiusKm))
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return tourApiSpotDtos;

    }

    // 주어진 위치가 반경 내에 있는지 확인하는 메서드
    private boolean isWithinRadius(double spotLat, double spotLng, double userLat, double userLng, double radiusKm) {
        double distance = distanceBetweenCoordinates(spotLat, spotLng, userLat, userLng);
        return distance <= radiusKm;
    }

    // 두 지점 사이의 거리를 계산하는 메서드(킬로미터 단위)
    private double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0; // 지구 반지름 (킬로미터)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

    // TourApiSpotEntity를 TourApiSpotResponseDto로 매핑하는 메서드
    private TourApiSpotResponseDto mapToDto(TourApiContent spot) {
        TourApiCategory3 tourApiCategory3 = tourApiCategory3Repository.findById(spot.getTourApiCategory3().getId())
                .orElseThrow(() -> new EntityNotFoundException("TourApiCategory3 not found categoryId: " + spot.getTourApiCategory3().getId()));

        TourApiContentInfo tourApiContentInfo = tourApiContentInfoRepository.findById(spot.getTourApiContentInfo().getId())
                .orElseThrow(EntityNotFoundException::new);

        return new TourApiSpotResponseDto(
                spot.getId(),
                tourApiCategory3.getCategoryName(),
                spot.getUpdatedAt(),
                spot.getLatitude(),
                spot.getLongitude(),
                tourApiContentInfo.getTitle()
        );
    }

}
