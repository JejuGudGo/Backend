package com.gudgo.jeju.global.data.tourAPI.common.service;

import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.common.repository.DataConfigurationRepository;
import com.gudgo.jeju.global.data.tourAPI.common.entity.*;
import com.gudgo.jeju.global.data.tourAPI.common.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadCsvService {

    private final TourApiContentTypeRepository contentTypeRepository;
    private final TourApiCategory1Repository category1Repository;
    private final TourApiCategory2Repository category2Repository;
    private final TourApiCategory3Repository category3Repository;
    private final DataConfigurationRepository dataConfigurationRepository;

    public void loadTourApiCommonCsvData() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("TourDataCommon")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            Map<String, TourApiContentType> contentTypeMap = new HashMap<>();
            Map<String, TourApiCategory1> category1Map = new HashMap<>();
            Map<String, TourApiCategory2> category2Map = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/api.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String contentTypeId = data[0];
                    String contentTypeName = data[1];
                    String cat1 = data[2];
                    String cat1Name = data[3];
                    String cat2 = data[4];
                    String cat2Name = data[5];
                    String cat3 = data[6];
                    String cat3Name = data[7];

                    TourApiContentType contentType = contentTypeMap.computeIfAbsent(contentTypeId,
                            id -> contentTypeRepository.save(
                                    TourApiContentType.builder()
                                            .contentType(contentTypeId)
                                            .title(contentTypeName)
                                            .build()));

                    TourApiCategory1 category1 = category1Map.computeIfAbsent(cat1,
                            code -> category1Repository.save(
                                    TourApiCategory1.builder()
                                            .categoryCode(cat1)
                                            .categoryName(cat1Name)
                                            .tourApiContentType(contentType)
                                            .build()));

                    TourApiCategory2 category2 = category2Map.computeIfAbsent(cat2,
                            code -> category2Repository.save(
                                    TourApiCategory2.builder()
                                            .categoryCode(cat2)
                                            .categoryName(cat2Name)
                                            .tourApiCategory1(category1)
                                            .build()));

                    category3Repository.save(
                            TourApiCategory3.builder()
                                    .categoryCode(cat3)
                                    .categoryName(cat3Name)
                                    .tourApiCategory2(category2)
                                    .build());
                }
            }

            DataConfiguration dataConfiguration = DataConfiguration.builder()
                    .configKey("TourDataCommon")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataConfigurationRepository.save(dataConfiguration);

        } else {
            log.info("===============================================================================");
            log.info("TourApiCommonCsvData is already loaded");
            log.info("===============================================================================");
        }
    }
}