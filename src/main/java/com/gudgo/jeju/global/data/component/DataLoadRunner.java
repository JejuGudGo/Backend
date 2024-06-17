package com.gudgo.jeju.global.data.component;

import com.gudgo.jeju.global.data.nickname.service.NicknameDataService;
import com.gudgo.jeju.global.data.olle.service.HaYoungOlleDatabaseService;
import com.gudgo.jeju.global.data.olle.service.HaYoungOlleSpotDatabaseService;
import com.gudgo.jeju.global.data.olle.service.JejuOlleDatabaseService;
import com.gudgo.jeju.global.data.tourAPI.common.service.LoadCsvService;
import com.gudgo.jeju.global.data.tourAPI.spot.service.TourApiRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoadRunner implements CommandLineRunner {

    private final LoadCsvService loadCsvService;
    private final TourApiRequestService tourApiRequestService;
    private final JejuOlleDatabaseService jejuOlleDatabaseService;
    private final NicknameDataService nicknameDataService;
    private final HaYoungOlleDatabaseService haYoungOlleDatabaseService;
    private final HaYoungOlleSpotDatabaseService haYoungOlleSpotDatabaseService;

    @Override
    public void run(String... args) throws Exception {
        loadCsvService.loadTourApiCommonCsvData();
        tourApiRequestService.prepareTourApiData();

        jejuOlleDatabaseService.convertGpxToDatabase();
        haYoungOlleDatabaseService.loadHaYoungData();
        haYoungOlleSpotDatabaseService.loadHaYoungOlleSpotCsvData();

        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
    }
}
