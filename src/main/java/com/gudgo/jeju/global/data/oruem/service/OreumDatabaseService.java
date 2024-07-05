package com.gudgo.jeju.global.data.oruem.service;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import com.gudgo.jeju.domain.tourApi.repository.TourApiCategory1Repository;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.domain.oreum.entity.Oreum;
import com.gudgo.jeju.domain.oreum.repository.OreumDataRepository;
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
                    String title = data[0];
                    String address = data[1];
                    double latitude = Double.parseDouble(data[2]);
                    double longitude = Double.parseDouble(data[3]);
                    String content = data[4];
                    LocalDate updatedAt = LocalDate.parse(data[5]);
                    String categoryId = data[6];


                    TourApiCategory1 category = tourApiCategory1Repository.findById(categoryId)
                            .orElseThrow(EntityNotFoundException::new);

                    Oreum oreum = Oreum.builder()
                            .title(title)
                            .address(address)
                            .latitude(latitude)
                            .longitude(longitude)
                            .content(content)
                            .updatedAt(updatedAt)
                            .tourApiCategory1(category)
                            .build();

                    oreumDataRepository.save(oreum);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("JejuOreumData")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);

            } else if (!checkDataConfig.isConfigValue()) {
                checkDataConfig = checkDataConfig.withConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);

            } else {
                log.info("===============================================================================");
                log.info("Jeju Oreum is already loaded");
                log.info("===============================================================================");
            }
        }
    }

}
