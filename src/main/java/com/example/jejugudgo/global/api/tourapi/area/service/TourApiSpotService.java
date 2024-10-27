package com.example.jejugudgo.global.api.tourapi.area.service;

import com.example.jejugudgo.global.api.tourapi.area.dto.TourApiSpotResponse;
import com.example.jejugudgo.global.api.tourapi.area.repository.TourApiSpotRepository;
import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiContentType;
import com.example.jejugudgo.global.api.tourapi.area.repository.TourApiContentTypeRepository;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiSpots;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourApiSpotService {
    private final TourApiContentTypeRepository tourApiContentTypeRepository;
    private final TourApiSpotRepository tourApiSpotRepository;
    private final TourApiSpotDataRequestService tourApiSpotDataRequestService;

    public void loadTourApiContentTypeIds() {
        List<ContentType> contentTypes = ContentType.getAllContentTypeIds();

        for (ContentType contentType : contentTypes) {
            if (tourApiContentTypeRepository.findByContentType(contentType) == null) {
                TourApiContentType tourApiContentType = TourApiContentType.builder()
                        .contentType(contentType)
                        .build();

                tourApiContentTypeRepository.save(tourApiContentType);
            }
        }
    }

    public TourApiSpotResponse getSpotDetail(Long tourApiSpotId) throws IOException {
        TourApiSpots tourApiSpots = tourApiSpotRepository.findById(tourApiSpotId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        if (tourApiSpots.getSummary() == null) {
            String contentTypeId = tourApiSpots.getTourApiContentType().getContentType().getContentTypeId();
            tourApiSpotDataRequestService.requestSpotData(contentTypeId, tourApiSpots.getContentId());
        }

        TourApiSpotResponse response = new TourApiSpotResponse(
                tourApiSpots.getTitle(),
                tourApiSpots.getContentId(),
                tourApiSpots.getSummary(),
                tourApiSpots.getImageUrl(),
                tourApiSpots.getAddress(),
                tourApiSpots.getLatitude(),
                tourApiSpots.getLongitude(),
                tourApiSpots.getPhone(),
                tourApiSpots.getHomepage(),
                tourApiSpots.getFee(),
                tourApiSpots.getOpeningHours(),
                tourApiSpots.getEventStartDate(),
                tourApiSpots.getEventEndDate(),
                tourApiSpots.getEventContent(),
                tourApiSpots.getEventPlace(),
                tourApiSpots.getEventFee(),
                tourApiSpots.getSponsor()
        );

        return response;
    }
}
