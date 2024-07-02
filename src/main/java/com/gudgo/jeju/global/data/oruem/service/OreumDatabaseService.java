package com.gudgo.jeju.global.data.oruem.service;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import com.gudgo.jeju.domain.tourApi.repository.TourApiCategory1Repository;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.oruem.entity.OreumData;
import com.gudgo.jeju.global.data.oruem.repository.OreumDataRepository;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class OreumDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final OreumDataRepository oreumDataRepository;
    private final TourApiCategory1Repository tourApiCategory1Repository;

    @Transactional
    public void loadJejuOreumCsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("JejuOreumData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/jejuOreum.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String title = data[1];
                    String address = data[2];
                    double latitude = Double.parseDouble(data[5]);
                    double longitude = Double.parseDouble(data[6]);
                    String content = data[7];

                    TourApiCategory1 category = tourApiCategory1Repository.findByCategoryName("자연")
                            .orElseThrow(EntityNotFoundException::new);

                    OreumData oreumData = OreumData.builder()
                            .title(title)
                            .address(address)
                            .latitude(latitude)
                            .longitude(longitude)
                            .content(content)
                            .updatedAt(LocalDate.now())
                            .tourApiCategory1(category)
                            .build();

                    oreumDataRepository.save(oreumData);
                }

                if (checkDataConfig == null) {
                    DataConfiguration dataConfiguration = DataConfiguration.builder()
                            .configKey("JejuOreumData")
                            .configValue(true)
                            .updatedAt(LocalDate.now())
                            .build();

                    dataConfigurationRepository.save(dataConfiguration);
                } else if (!checkDataConfig.isConfigValue()) {
                    checkDataConfig.withConfigValue(true);
                    dataConfigurationRepository.save(checkDataConfig);
                } else {
                    log.info("===============================================================================");
                    log.info("Jeju Oreum is already loaded");
                    log.info("===============================================================================");
                }
            }
        }
    }

}
