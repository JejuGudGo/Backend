package com.gudgo.jeju.global.data.oruem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.oreum.entity.Oreum;
import com.gudgo.jeju.domain.oreum.repository.OreumDataRepository;
import com.gudgo.jeju.domain.trail.entity.TrailType;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OreumRequestService {

    @Value("${public-data.api.key}")
    private String KEY;

    @Value("${public-data.api.base-url}")
    private String BASE_URL;

    private final DataConfigurationRepository dataConfigurationRepository;
    private final OreumDataRepository oreumDataRepository;

    public void prepareOreumData() throws IOException {
        log.info("===============================================================================");
        log.info("Starting to prepare Oreum API data...");
        log.info("===============================================================================");

        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("OreumCommon")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            requestOreumList();

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("OreumCommon")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);
            } else if (!checkDataConfig.isConfigValue()) {
                checkDataConfig.withConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);
            }
        } else {
            log.info("===============================================================================");
            log.info("OreumCommonData is already loaded");
            log.info("===============================================================================");
        }
    }

    @Transactional
    public void requestOreumList() throws IOException {
        String requestUrl = BASE_URL + "?page=1&perPage=90&serviceKey=" + KEY;
        Map<String, Object> response = sendGetRequest(requestUrl);

        if (response != null && response.containsKey("data")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("data");

            List<Oreum> oreumList = new ArrayList<>();

            for (Map<String, Object> item : items) {
                Oreum oreum = Oreum.builder()
                        .title(item.get("오름명").toString())
                        .address(item.get("위치").toString())
                        .latitude(Double.parseDouble(item.get("위도").toString()))
                        .longitude(Double.parseDouble(item.get("경도").toString()))
                        .content(item.get("설명").toString())
                        .updatedAt(LocalDate.now())
                        .type(TrailType.TRAIL02)
                        .build();

                oreumList.add(oreum);
            }

            oreumDataRepository.saveAll(oreumList);
            log.info("Saved {} Oreum entities", oreumList.size());
        }
    }

    private Map<String, Object> sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.toString(), Map.class);
    }
}