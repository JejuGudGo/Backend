package com.example.jejugudgo.global.data.home;

import com.example.jejugudgo.domain.event.entity.Event;
import com.example.jejugudgo.domain.event.entity.EventStatus;
import com.example.jejugudgo.domain.event.repository.EventRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
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
                        .map(fields -> {
                            try {
                                LocalDate startDate = parseDates(fields[3])[0];
                                LocalDate endDate = parseDates(fields[3])[1];

                                return Event.builder()
                                        .title(fields[1])
                                        .host(fields[2])
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .eventStatus(EventStatus.getEventStatus(startDate, endDate))
                                        .thumbnail(fields[4])
                                        .link(fields[5])
                                        .build();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                eventRepository.saveAll(events);

            } catch (IOException | CsvException e) {
                e.printStackTrace();
                throw e;
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

    public LocalDate[] parseDates(String dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] parts = dates.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid dates format");
        }
        LocalDate startDate = LocalDate.parse(parts[0].trim(), formatter);
        LocalDate endDate = LocalDate.parse(parts[1].trim(), formatter);
        return new LocalDate[]{startDate, endDate};
    }
}
