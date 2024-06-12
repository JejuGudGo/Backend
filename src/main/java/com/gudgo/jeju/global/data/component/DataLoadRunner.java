package com.gudgo.jeju.global.data.component;

import com.gudgo.jeju.global.data.tourAPI.common.service.TourApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoadRunner implements CommandLineRunner {

    private final TourApiService tourApiService;

    @Override
    public void run(String... args) throws IOException {
        log.info("Starting CSV data load...");
        tourApiService.loadTourApiCommonCsvData();
        log.info("CSV data load completed.");
    }
}
