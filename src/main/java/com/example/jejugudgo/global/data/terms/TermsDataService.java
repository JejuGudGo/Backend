package com.example.jejugudgo.global.data.terms;

import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class TermsDataService {

    private final DataCommandLogRepository dataCommandLogRepository;
    private final TermsRepository termsRepository;

    public void loadTermsCsvToDatabase() {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("TermsData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/terms/terms.csv").getInputStream()))) {

                List<Terms> terms = csvReader.readAll().stream()
                        .map(fields -> Terms.builder()
                                .title(fields[0].trim())
                                .content(fields[1].trim())
                                .build())
                        .collect(Collectors.toList());

                termsRepository.saveAll(terms);

            } catch (Exception e) {
                e.printStackTrace();
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("TermsData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All Term data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Term data is already loaded");
            log.info("===============================================================================");
        }
    }
}