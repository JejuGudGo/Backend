package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.api.tourapi.common.service.TourApiDataRequestService;
import com.example.jejugudgo.global.api.tourapi.common.service.TourApiService;
import com.example.jejugudgo.global.data.home.EventDataService;
import com.example.jejugudgo.global.data.home.TrailDataService;
import com.example.jejugudgo.global.data.nickname.service.NicknameDataService;
import com.example.jejugudgo.global.data.terms.TermsDataService;
import com.example.jejugudgo.global.exception.service.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoadRunner implements CommandLineRunner {
    private final NicknameDataService nicknameDataService;
    private final TermsDataService termsDataService;
    private final TrailDataService trailDataService;
    private final EventDataService eventDataService;
    private final ApiResponseService responseService;
    private final TourApiService tourApiService;
    private final TourApiDataRequestService tourApiDataRequestService;

    @Override
    public void run(String... args) throws Exception {
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();
        trailDataService.loadTrailCsvToDatabase();
        eventDataService.loadEventCsvToDatabase();
        responseService.loadApiResponse();
        tourApiService.loadTourApiContentTypeIds();
        tourApiDataRequestService.loadTourApiData();
    }
}
