package com.gudgo.jeju.domain.course.validation;

import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.gudgo.jeju.global.data.tourAPI.service.TourApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SpotValidationService {
    private final TourApiRequestService requestService;

    public void validateIsCurrentData(TourApiContent subContentType) throws IOException {
        String currentUpdatedDate = requestService.ExtractUpdatedDate(subContentType.getId(), subContentType.getContentTypeId());

        if (!subContentType.getUpdatedAt().equals(currentUpdatedDate)) {
            requestService.requestSpotDetail(subContentType.getId(), subContentType.getContentTypeId());
        }
    }
}
