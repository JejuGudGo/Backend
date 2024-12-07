package com.example.jejugudgo.global.data.event;

import com.example.jejugudgo.domain.event.entity.Event;
import com.example.jejugudgo.domain.event.entity.EventStatus;
import com.example.jejugudgo.domain.event.repository.EventRepository;
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
public class EventDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final EventRepository eventRepository;

    public void loadEventCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("EventData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/home/event.csv").getInputStream()))) {
                List<Event> events = csvReader.readAll().stream()
                        .skip(1)
                        .map(fields -> { // id,status,title,host,time,thumbnailUrl,hompage
                            try {
                                return Event.builder()
                                        .id(Long.parseLong(fields[0]))
                                        .title(fields[2])
                                        .host(fields[3])
                                        .time(fields[4])
                                        .thumbnailUrl(fields[5])
                                        .homepage(fields[6])
                                        .eventStatus(EventStatus.fromInput(fields[1]))
                                        .build();
                            } catch (Exception e) {
                                throw new CustomException(RetCode.RET_CODE92);
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                eventRepository.saveAll(events);

            } catch (IOException | CsvException e) {
                throw new CustomException(RetCode.RET_CODE92);
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("EventData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All event data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Event data is already loaded");
            log.info("===============================================================================");
        }
    }
}
