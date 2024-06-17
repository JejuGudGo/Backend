package com.gudgo.jeju.global.data.nickname.service;

import com.gudgo.jeju.global.data.nickname.entity.Adjective;
import com.gudgo.jeju.global.data.nickname.entity.Noun;
import com.gudgo.jeju.global.data.nickname.repository.AdjectiveRepository;
import com.gudgo.jeju.global.data.nickname.repository.NounRepository;
import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.common.repository.DataConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NicknameDataService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final AdjectiveRepository adjectiveRepository;
    private final NounRepository nounRepository;

    public void loadAdjectiveCsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("AdjectiveData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/adjective.csv").getInputStream()));
                List<Adjective> adjectives = br.lines()
                        .map(line -> Adjective.builder().adjective(line).build())
                        .collect(Collectors.toList());

                adjectiveRepository.saveAll(adjectives);

            } catch (Exception e) {
                e.printStackTrace();
            }

            DataConfiguration dataConfiguration = DataConfiguration.builder()
                    .configKey("AdjectiveData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataConfigurationRepository.save(dataConfiguration);

            log.info("===============================================================================");
            log.info("All Adjective data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Adjective data is already loaded");
            log.info("===============================================================================");
        }
    }

    public void loadNounCsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("NounData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/noun.csv").getInputStream()));
                List<Noun> nouns = br.lines()
                        .map(line -> Noun.builder().noun(line).build())
                        .collect(Collectors.toList());

                nounRepository.saveAll(nouns);

            } catch (Exception e) {
                e.printStackTrace();
            }

            DataConfiguration dataConfiguration = DataConfiguration.builder()
                    .configKey("NounData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataConfigurationRepository.save(dataConfiguration);

            log.info("===============================================================================");
            log.info("All Noun data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Noun data is already loaded");
            log.info("===============================================================================");
        }
    }
}
