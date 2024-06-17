package com.gudgo.jeju.global.data.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.global.data.tourAPI.common.dto.TourApiSubContentTypeDto;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
import com.gudgo.jeju.global.data.tourAPI.common.repository.TourApiSubContentTypeRepository;
import com.gudgo.jeju.global.data.tourAPI.spot.dto.TourApiSpotDto;
import com.gudgo.jeju.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourApiSpotCachingService {
    private final TourApiSubContentTypeRepository tourApiSubContentTypeRepository;
    private final RedisUtil redisUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setSpotData(String contentId) throws JsonProcessingException {
        TourApiSubContentType tourApiSubContentType = tourApiSubContentTypeRepository.findById(contentId).orElse(null);

        TourApiSpotDto tourApiSpotDto = new TourApiSpotDto(
                tourApiSubContentType.getTourApiSpotData().getId(),
                tourApiSubContentType.getTourApiSpotData().getTitle(),
                tourApiSubContentType.getTourApiSpotData().getAddress(),
                tourApiSubContentType.getTourApiSpotData().getContent(),
                tourApiSubContentType.getTourApiSpotData().getPageUrl(),
                tourApiSubContentType.getTourApiSpotData().getInfo(),
                tourApiSubContentType.getTourApiSpotData().getCloseDay(),
                tourApiSubContentType.getTourApiSpotData().getOrganizerInfo(),
                tourApiSubContentType.getTourApiSpotData().getOrganizeNumber(),
                tourApiSubContentType.getTourApiSpotData().getEventStartDate(),
                tourApiSubContentType.getTourApiSpotData().getEventEndDate(),
                tourApiSubContentType.getTourApiSpotData().getFee(),
                tourApiSubContentType.getTourApiSpotData().getTime(),
                tourApiSubContentType.getTourApiSpotData().getPark(),
                tourApiSubContentType.getTourApiSpotData().getRentStroller(),
                tourApiSubContentType.getTourApiSpotData().getAvailablePet(),
                tourApiSubContentType.getTourApiSpotData().getEventContent(),
                tourApiSubContentType.getTourApiSpotData().getEventFee(),
                tourApiSubContentType.getTourApiSpotData().getEventPlace(),
                tourApiSubContentType.getTourApiSpotData().getGuideService(),
                tourApiSubContentType.getTourApiSpotData().getToilet(),
                tourApiSubContentType.getTourApiSpotData().getReserveInfo()
        );

        TourApiSubContentTypeDto tourApiSubContentTypeDto = new TourApiSubContentTypeDto(
                tourApiSubContentType.getLatitude(),
                tourApiSubContentType.getLongitude(),
                tourApiSubContentType.getUpdatedAt(),
                tourApiSpotDto
        );

        if (tourApiSubContentType != null) {
            String value = objectMapper.writeValueAsString(tourApiSubContentTypeDto);
            redisUtil.setData(contentId, value);
        }
    }
}
