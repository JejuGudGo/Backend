package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.api.tourapi.area.service.TourApiSpotDataRequestService;
import com.example.jejugudgo.global.api.tourapi.area.service.TourApiSpotService;
import com.example.jejugudgo.global.data.home.EventDataService;
import com.example.jejugudgo.global.data.home.TrailDataService;
import com.example.jejugudgo.global.data.nickname.service.NicknameDataService;
import com.example.jejugudgo.global.data.olle.service.OlleCsvDataService;
import com.example.jejugudgo.global.data.olle.service.OlleGpxDataService;
import com.example.jejugudgo.global.data.terms.TermsDataService;
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
    private final TourApiSpotService tourApiSpotService;
    private final TourApiSpotDataRequestService tourApiSpotDataRequestService;
    private final OlleCsvDataService olleDataService;
    private final OlleGpxDataService olleGpxDataService;
  
    @Override
    public void run(String... args) throws Exception {
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();
        trailDataService.loadTrailCsvToDatabase();
        eventDataService.loadEventCsvToDatabase();
        tourApiSpotService.loadTourApiContentTypeIds();
        tourApiSpotDataRequestService.loadTourApiData();
        olleDataService.loadOlleCourseData();
        olleGpxDataService.loadGpxData();
    }
}
