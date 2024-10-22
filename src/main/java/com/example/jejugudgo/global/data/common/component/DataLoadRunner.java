package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.data.home.EventDataService;
import com.example.jejugudgo.global.data.home.TrailDataService;
import com.example.jejugudgo.global.data.nickname.service.NicknameDataService;
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

    @Override
    public void run(String... args) throws Exception {
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();
        trailDataService.loadTrailCsvToDatabase();
        eventDataService.loadEventCsvToDatabase();
    }
}
