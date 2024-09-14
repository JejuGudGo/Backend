package com.gudgo.jeju.global.data.label.service;

import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.label.entity.Label;
import com.gudgo.jeju.global.data.label.repository.LabelRepository;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LabelDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final LabelRepository labelRepository;

    @Transactional
    public void loadLabelCsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("Label")
                .orElse(null);
        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new ClassPathResource("csv/label.csv").getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String code = data[0];
                    String title = data[1];

                    Label label = Label.builder()
                            .code(code)
                            .title(title)
                            .build();
                    labelRepository.save(label);
                }

                if (checkDataConfig == null) {
                    DataConfiguration dataConfiguration = DataConfiguration.builder()
                            .configKey("Label")
                            .configValue(true)
                            .updatedAt(LocalDate.now())
                            .build();
                    dataConfigurationRepository.save(dataConfiguration);
                } else if (!checkDataConfig.isConfigValue()) {
                    checkDataConfig.withConfigValue(true);
                    dataConfigurationRepository.save(checkDataConfig);
                }
            }
        } else {
            log.info("===============================================================================");
            log.info("Label is already loaded");
            log.info("===============================================================================");
        }
    }
}