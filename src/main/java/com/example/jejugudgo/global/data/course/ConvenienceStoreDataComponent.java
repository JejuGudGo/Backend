package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.recommend.entity.ConvenienceStore;
import com.example.jejugudgo.domain.course.recommend.repository.ConvenienceStoreRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@RequiredArgsConstructor
public class ConvenienceStoreDataComponent {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final ConvenienceStoreRepository convenienceStoreRepository;

    public void loadConvenienceStoreCsvToDatabase() throws IOException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("ConvenienceStoreData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/course/convenience_store.csv").getInputStream()))) {
                List<ConvenienceStore> convenienceStores = csvReader.readAll()
                        .stream()
                        .skip(1)
                        .map(fields -> {
                            try { //title,address,latitude,longitude
                                double[] convertedCoordinates = convertCoordinates(Double.parseDouble(fields[3]), Double.parseDouble(fields[2]));

                                return ConvenienceStore.builder()
                                        .title(fields[0])
                                        .address(fields[1])
                                        .latitude(convertedCoordinates[0])
                                        .longitude(convertedCoordinates[1])
                                        .build();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                convenienceStoreRepository.saveAll(convenienceStores);

            } catch (IOException | CsvException e) {
                throw new CustomException(RetCode.RET_CODE92);
            }
            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("ConvenienceStoreData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All convenience store data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Convenience store data is already loaded");
            log.info("===============================================================================");
        }
    }

    private static double[] convertCoordinates (double x, double y) {
        final double R = 6378137.0;
        double longitude = x / R * (180 / Math.PI);
        double latitude = Math.atan(Math.sinh(y / R)) * (180 / Math.PI);
        return new double[]{latitude, longitude};
    }
}
