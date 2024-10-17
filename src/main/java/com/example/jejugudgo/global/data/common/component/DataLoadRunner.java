package com.example.jejugudgo.global.data.common.component;

import com.example.jejugudgo.global.data.nickname.service.NicknameDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoadRunner implements CommandLineRunner {
    private final NicknameDataService nicknameDataService;

    @Override
    public void run(String... args) throws Exception {
        nicknameDataService.loadAdjectiveCsvToDatabase();
        nicknameDataService.loadNounCsvToDatabase();
    }
}
