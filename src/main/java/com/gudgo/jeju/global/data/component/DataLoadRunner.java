package com.gudgo.jeju.global.data.component;

import com.gudgo.jeju.global.data.nickname.service.NicknameDataService;
import com.gudgo.jeju.global.data.olle.service.GpxToDatabaseService;
import com.gudgo.jeju.global.data.tourAPI.common.service.LoadCsvService;
import com.gudgo.jeju.global.data.tourAPI.spot.service.TourApiRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoadRunner implements CommandLineRunner {

    private final LoadCsvService loadCsvService;
    private final TourApiRequestService tourApiRequestService;
    private final GpxToDatabaseService gpxToDatabaseService;
    private final NicknameDataService nicknameDataService;

    @Override
    public void run(String... args) throws IOException {
        loadCsvService.loadTourApiCommonCsvData();
//        tourApiRequestService.loadTourApiCsvData();
        gpxToDatabaseService.convertGpxToDatabase();
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
    }
}
