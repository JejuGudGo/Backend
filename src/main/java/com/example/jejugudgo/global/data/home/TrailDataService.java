package com.example.jejugudgo.global.data.home;

import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.trail.entity.TrailType;
import com.example.jejugudgo.domain.trail.repository.TrailRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;


@Service
@RequiredArgsConstructor
public class TrailDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final TrailRepository trailRepository;

    public void loadTrailCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("TrailData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/home/trail.csv").getInputStream()))) {

                List<Trail> trails = csvReader.readAll().stream()
                        .map(fields -> {
                            try {
                                return Trail.builder()
                                        .trailType(TrailType.fromCode(fields[1].trim()))
                                        .title(fields[2])
                                        .latitude(parseCoordinates(fields[3])[0])
                                        .longitude(parseCoordinates(fields[3])[1])
                                        .content(fields[4])
                                        .address(fields[5])
                                        .phoneNumber(fields[6])
                                        .homepageUrl(fields[7])
                                        .businessHours(fields[8])
                                        .fee(fields[9])
                                        .duration(fields[10])
                                        .reference(fields[12])
                                        .build();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                trailRepository.saveAll(trails);

            } catch (IOException | CsvException e) {
                e.printStackTrace();
                throw e;
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("TrailData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All trail data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Trail data is already loaded");
            log.info("===============================================================================");
        }
    }

    public double[] parseCoordinates(String coordinates) {
        String[] parts = coordinates.split(", ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid coordinate format");
        }
        return new double[]{Double.parseDouble(parts[0]), Double.parseDouble(parts[1])};
    }
}