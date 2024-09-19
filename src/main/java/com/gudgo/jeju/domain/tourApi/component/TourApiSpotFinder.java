package com.gudgo.jeju.domain.tourApi.component;

import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotResponseDto;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContentImage;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContentInfo;
import com.gudgo.jeju.domain.tourApi.query.TourApiSearchQueryService;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentImageRepository;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentRepository;
import com.gudgo.jeju.global.data.tourAPI.service.TourApiRequestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TourApiSpotFinder {
    private final TourApiSearchQueryService searchQueryService;
    private final TourApiRequestService requestService;
    private final TourApiContentRepository tourApiContentRepository;
    private final TourApiContentImageRepository tourApiContentImageRepository;

    @Transactional
    public List<TourApiSpotResponseDto> searchTourApiSpots(double latitude, double longitude, String contentTypeId) {
        final double radiusKm = 2.0;

        List<TourApiContent> tourApiSpots = searchQueryService.searchTourApiSpots(latitude, longitude, radiusKm, contentTypeId);

        List<TourApiSpotResponseDto> tourApiSpotDtos = tourApiSpots.stream()
                .filter(spot -> isWithinRadius(spot.getLatitude(), spot.getLongitude(), latitude, longitude, radiusKm))
                .map(spot -> {
                    try {
                        return mapToDto(spot, contentTypeId);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());

        if (tourApiSpotDtos.isEmpty()) {
            throw new EntityNotFoundException("INVALID_VALUE_01");
        }

        log.debug("Found {} spots within radius", tourApiSpotDtos.size());
        return tourApiSpotDtos;
    }

    private boolean isWithinRadius(double spotLat, double spotLng, double userLat, double userLng, double radiusKm) {
        double distance = distanceBetweenCoordinates(spotLat, spotLng, userLat, userLng);
        return distance <= radiusKm;
    }

    private double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

    private TourApiSpotResponseDto mapToDto(TourApiContent spot, String contentTypeId) throws IOException {

        TourApiContentInfo tourApiContentInfo = spot.getTourApiContentInfo();
        if (tourApiContentInfo == null) {
            requestService.requestSpotDetail(spot.getId(), contentTypeId);
        }

        List<TourApiContentImage> images = tourApiContentImageRepository.findByTourApiContentInfoId(tourApiContentInfo.getId());
        String imgUrl = "None";

        if (!images.isEmpty()) imgUrl = images.get(0).getImageUrl();

        return new TourApiSpotResponseDto(
                spot.getId(),
                spot.getUpdatedAt(),
                spot.getLatitude(),
                spot.getLongitude(),
                tourApiContentInfo.getTitle(),
                imgUrl,
                tourApiContentInfo.getContent()
        );
    }
}