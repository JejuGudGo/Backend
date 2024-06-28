package com.gudgo.jeju.global.data.tourAPI.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.tourApi.dto.TourApiContentDto;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.domain.tourApi.repository.TourApiContentRepository;
import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotDto;
import com.gudgo.jeju.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourApiSpotCachingService {
    private final TourApiContentRepository tourApiContentRepository;
    private final RedisUtil redisUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setSpotData(String contentId) throws JsonProcessingException {
        TourApiContent tourApiContent = tourApiContentRepository.findById(contentId)
                .orElse(null);

        if (tourApiContent != null) {
            TourApiSpotDto tourApiSpotDto = new TourApiSpotDto(
                    tourApiContent.getTourApiContentInfo().getId(),
                    tourApiContent.getTourApiContentInfo().getTitle(),
                    tourApiContent.getTourApiContentInfo().getAddress(),
                    tourApiContent.getTourApiContentInfo().getContent(),
                    tourApiContent.getTourApiContentInfo().getPageUrl(),
                    tourApiContent.getTourApiContentInfo().getInfo(),
                    tourApiContent.getTourApiContentInfo().getCloseDay(),
                    tourApiContent.getTourApiContentInfo().getOrganizerInfo(),
                    tourApiContent.getTourApiContentInfo().getOrganizeNumber(),
                    tourApiContent.getTourApiContentInfo().getEventStartDate(),
                    tourApiContent.getTourApiContentInfo().getEventEndDate(),
                    tourApiContent.getTourApiContentInfo().getFee(),
                    tourApiContent.getTourApiContentInfo().getTime(),
                    tourApiContent.getTourApiContentInfo().getPark(),
                    tourApiContent.getTourApiContentInfo().getRentStroller(),
                    tourApiContent.getTourApiContentInfo().getAvailablePet(),
                    tourApiContent.getTourApiContentInfo().getEventContent(),
                    tourApiContent.getTourApiContentInfo().getEventFee(),
                    tourApiContent.getTourApiContentInfo().getEventPlace(),
                    tourApiContent.getTourApiContentInfo().getGuideService(),
                    tourApiContent.getTourApiContentInfo().getToilet(),
                    tourApiContent.getTourApiContentInfo().getReserveInfo()
            );

            TourApiContentDto tourApiContentDto = new TourApiContentDto(
                    tourApiContent.getLatitude(),
                    tourApiContent.getLongitude(),
                    tourApiContent.getUpdatedAt(),
                    tourApiSpotDto
            );

            String value = objectMapper.writeValueAsString(tourApiContentDto);
            redisUtil.setData(contentId, value);
        }
    }
}
