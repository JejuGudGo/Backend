package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.data.course.*;
import com.example.jejugudgo.global.data.event.EventDataService;
import com.example.jejugudgo.global.data.term.component.TermDataComponent;
import com.example.jejugudgo.global.data.nickname.component.NicknameDataComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoadRunner implements CommandLineRunner {
    private final NicknameDataComponent nicknameDataComponent;
    private final TermDataComponent termsDataService;
    private final EventDataService eventDataService;
    private final TrailDataService trailDataService;

    private final OlleCourseDataService olleCourseDataService;
    private final OlleSpotDataService olleSpotDataService;
    private final OlleCourseTagDataService olleCourseTagDataService;
    private final OlleGpxDataService olleGpxDataService;
    private final HayongOlleGpxDataService hayongOlleGpxDataService;

    @Override
    public void run(String... args) throws Exception {
        nicknameDataComponent.loadAdjectiveCsvToDatabase();
        nicknameDataComponent.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();

        eventDataService.loadEventCsvToDatabase();

        trailDataService.loadTrailCsvToDatabase();
        olleCourseDataService.loadOlleCourseCsvToDatabase();
        olleSpotDataService.loadOlleSpotCsvToDatabase();
        olleCourseTagDataService.loadOlleTagCsvToDatabase();
        olleGpxDataService.loadOlleGpxCsvToDatabase();
        hayongOlleGpxDataService.loadHaYoungOlleSpotCsvData();
    }
}
