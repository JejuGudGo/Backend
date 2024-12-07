package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.enums.TrailTag;
import com.example.jejugudgo.domain.course.common.repository.TrailRepository;
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
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@RequiredArgsConstructor
public class TrailDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final TrailRepository trailRepository;

    public void loadTrailCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("TrailData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/course/trail.csv").getInputStream()))) {
                List<Trail> trails = csvReader.readAll().stream()
                        .skip(1)
                        .map(fields -> { // id,tag,title,address,content,tel,homepage,opentime,fee,time,thumbnailUrl,latitude,longitude
                            try {
                                String tel = fields[5].isBlank() ? null : fields[5];
                                String homepage = fields[6].isBlank() ? null : fields[6];
                                String openTime = fields[7].isBlank() ? null : fields[7];
                                String fee = fields[8].isBlank() ? null : fields[8];
                                String time = fields[9].isBlank() ? null : fields[9];

                                Double latitude = (fields[11].isBlank() || fields[11].equalsIgnoreCase("null")) ? null : Double.parseDouble(fields[11]);
                                Double longitude = (fields[12].isBlank() || fields[12].equalsIgnoreCase("null")) ? null : Double.parseDouble(fields[12]);

                                return Trail.builder()
                                        .id(Long.parseLong(fields[0]))
                                        .trailTag(fields[1].isBlank() ? null : TrailTag.fromInput(fields[1]))
                                        .title(fields[2].isBlank() ? "" : fields[2])
                                        .address(fields[3].isBlank() ? "" : fields[3])
                                        .content(fields[4].isBlank() ? "" : fields[4])
                                        .tel(tel)
                                        .homepage(homepage)
                                        .openTime(openTime)
                                        .fee(fee)
                                        .time(time)
                                        .thumbnailUrl(fields[10].isBlank() ? "" : fields[10])
                                        .latitude(latitude)
                                        .longitude(longitude)
                                        .build();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();
                trailRepository.saveAll(trails);

            } catch (IOException | CsvException e) {
                throw new CustomException(RetCode.RET_CODE92);
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
}