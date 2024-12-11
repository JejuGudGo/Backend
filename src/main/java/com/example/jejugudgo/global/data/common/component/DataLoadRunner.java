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
    private final TermDataComponent termsDataComponent;
    private final EventDataService eventDataComponent;
    private final TrailDataComponent trailDataComponent;

    private final OlleCourseDataComponent olleCourseDataComponent;
    private final OlleSpotDataService olleSpotDataComponent;
    private final OlleCourseTagDataComponent olleCourseTagDataComponent;
    private final OlleGpxDataService olleGpxDataComponent;
    private final HayongOlleGpxDataComponent hayongOlleGpxDataComponent;
    private final TourApiDataComponent tourApiDataComponent;

    @Override
    public void run(String... args) throws Exception {
        nicknameDataComponent.loadAdjectiveCsvToDatabase();
        nicknameDataComponent.loadNounCsvToDatabase();
        termsDataComponent.loadTermsCsvToDatabase();

        eventDataComponent.loadEventCsvToDatabase();

        trailDataComponent.loadTrailCsvToDatabase();
        olleCourseDataComponent.loadOlleCourseCsvToDatabase();
        olleCourseTagDataComponent.loadOlleTagCsvToDatabase();
        olleSpotDataComponent.loadOlleSpotCsvToDatabase();
        olleGpxDataComponent.loadOlleGpxCsvToDatabase();
        hayongOlleGpxDataComponent.loadHaYoungOlleSpotCsvData();
        tourApiDataComponent.loadTourApiData();
    }
}
