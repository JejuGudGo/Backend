package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.api.tourapi.area.service.TourApiSpotDataRequestService;
import com.example.jejugudgo.global.api.tourapi.area.service.TourApiSpotService;
import com.example.jejugudgo.global.data.home.EventDataService;
import com.example.jejugudgo.global.data.home.TrailDataService;
import com.example.jejugudgo.global.data.nickname.service.NicknameDataService;
import com.example.jejugudgo.global.data.olle.service.OlleDataService;
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
    private final TourApiSpotService tourApiSpotService;
    private final TourApiSpotDataRequestService tourApiSpotDataRequestService;
    private final OlleDataService olleDataService;
  
    @Override
    public void run(String... args) throws Exception {
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();
        trailDataService.loadTrailCsvToDatabase();
        eventDataService.loadEventCsvToDatabase();
        responseService.loadApiResponse();
        tourApiSpotService.loadTourApiContentTypeIds();
        tourApiSpotDataRequestService.loadTourApiData();
        olleDataService.loadOlleCourseData();
    }
}
