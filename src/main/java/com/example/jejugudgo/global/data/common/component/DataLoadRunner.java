package com.example.jejugudgo.global.data.common.component;

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

    @Override
    public void run(String... args) throws Exception {
        nicknameDataComponent.loadAdjectiveCsvToDatabase();
        nicknameDataComponent.loadNounCsvToDatabase();
        termsDataService.loadTermsCsvToDatabase();
    }
}
