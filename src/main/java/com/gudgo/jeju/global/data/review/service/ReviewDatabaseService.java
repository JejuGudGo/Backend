package com.gudgo.jeju.global.data.review.service;

import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewTag;
import com.gudgo.jeju.global.data.review.repository.ReviewRepository;
import com.gudgo.jeju.global.data.review.repository.ReviewTagRepository;
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
public class ReviewDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewTagRepository reviewTagRepository;

    @Transactional
    public void loadReviewCategory1CsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("Review")
                .orElse(null);
        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new ClassPathResource("csv/reviewCategory1.csv").getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String code = data[0];
                    String title = data[1];

                    Review review = Review.builder()
                            .code(code)
                            .title(title)
                            .build();

                    reviewRepository.save(review);
                }

                if (checkDataConfig == null) {
                    DataConfiguration dataConfiguration = DataConfiguration.builder()
                            .configKey("Review")
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
            log.info("Review is already loaded");
            log.info("===============================================================================");
        }

    }

    @Transactional
    public void loadReviewCategory2CsvToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("ReviewTag")
                .orElse(null);
        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new ClassPathResource("csv/reviewCategory2.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String code = data[0];
                    String title = data[1];

                    String reviewCode;

                    int codeNum = Integer.parseInt(data[0].replace("RT", "").trim().replaceAll("[^\\d]", ""));

                    if (codeNum >= 1 && codeNum <= 7) {
                        reviewCode = "R01";
                    } else if (codeNum >= 8 && codeNum <= 17) {
                        reviewCode = "R02";
                    } else {
                        reviewCode = "R03";
                    }

                    Review review = reviewRepository.findByCode(reviewCode)
                            .orElseThrow(EntityNotFoundException::new);

                    ReviewTag reviewTag = ReviewTag.builder()
                            .code(code)
                            .title(title)
                            .review(review)
                            .build();

                    reviewTagRepository.save(reviewTag);
                }

                if (checkDataConfig == null) {
                    DataConfiguration dataConfiguration = DataConfiguration.builder()
                            .configKey("ReviewTag")
                            .configValue(true)
                            .updatedAt(LocalDate.now())
                            .build();
                    dataConfigurationRepository.save(dataConfiguration);
                } else if (!checkDataConfig.isConfigValue()) {
                    checkDataConfig = checkDataConfig.withConfigValue(true);
                    dataConfigurationRepository.save(checkDataConfig);
                }
            }
        } else {
            log.info("===============================================================================");
            log.info("ReviewTag is already loaded");
            log.info("===============================================================================");
        }
    }



}