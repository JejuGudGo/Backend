package com.gudgo.jeju.global.data.common.component;

import com.gudgo.jeju.global.data.nickname.service.NicknameDataService;
import com.gudgo.jeju.global.data.olle.service.HaYoungOlleDatabaseService;
import com.gudgo.jeju.global.data.olle.service.HaYoungOlleSpotDatabaseService;
import com.gudgo.jeju.global.data.olle.service.JejuOlleDatabaseService;
import com.gudgo.jeju.global.data.olle.service.JejuOlleSpotDatabaseService;
import com.gudgo.jeju.global.data.tourAPI.service.TourApiRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoadRunner implements CommandLineRunner {

    private final TourApiRequestService.LoadCsvService loadCsvService;
    private final NicknameDataService nicknameDataService;
    private final TourApiRequestService tourApiRequestService;
    private final JejuOlleDatabaseService jejuOlleDatabaseService;
    private final JejuOlleSpotDatabaseService jejuOlleSpotDatabaseService;
    private final HaYoungOlleDatabaseService haYoungOlleDatabaseService;
    private final HaYoungOlleSpotDatabaseService haYoungOlleSpotDatabaseService;

    @Override
    public void run(String... args) throws Exception {
        loadCsvService.loadTourApiCommonCsvData();
        tourApiRequestService.prepareTourApiData();

        jejuOlleDatabaseService.convertGpxToDatabase();
        jejuOlleDatabaseService.addAdditionalData();
        jejuOlleSpotDatabaseService.loadJeJuOlleSpotCsvData();
        haYoungOlleDatabaseService.loadHaYoungData();
        haYoungOlleSpotDatabaseService.loadHaYoungOlleSpotCsvData();

        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
    }
}