package com.example.jejugudgo.domain.course.api.component;


import com.example.jejugudgo.domain.course.api.entity.TourApiContentType;
import com.example.jejugudgo.domain.course.api.entity.TourApiSpot;
import com.example.jejugudgo.domain.course.api.enums.ContentType;
import com.example.jejugudgo.domain.course.api.repository.TourApiContentTypeRepository;
import com.example.jejugudgo.domain.course.api.repository.TourApiSpotRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TourApiComponent {
    @Value("${tour.api.key}")
    private String KEY;
    private final String FRONT_URL = "http://apis.data.go.kr/B551011/KorService1/";

    private final TourApiSpotRepository tourApiSpotRepository;
    private final TourApiContentTypeRepository tourApiContentTypeRepository;
    private final DataCommandLogRepository dataCommandLogRepository;

    public void loadTourApiData() throws java.io.IOException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("TourApiSpotsData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            List<TourApiContentType> contentTypes = tourApiContentTypeRepository.findAll();
            for (TourApiContentType contentType : contentTypes) {
                requestRowNumber(contentType.getContentType().getContentTypeId());
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("TourApiSpotsData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All tour api spot data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("All tour api spot data is already uploaded!");
            log.info("===============================================================================");
        }
    }

    private void requestRowNumber(String contentTypeId) throws java.io.IOException {
        String requestUrl = FRONT_URL + "areaBasedList1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=" + "&pageNo=1" +
                "&listYN=N&arrange=A&areaCode=39&sigunguCode=&contentTypeId=" + contentTypeId +
                "&cat1=&cat2=&cat3=";

        Map<String, Object> response = sendGetRequest(requestUrl);
        if (response != null) {
            String rowNumber = "";
            Map<String, Object> responseBody = (Map<String, Object>) response.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null) {
                    if (body.containsKey("items")) {
                        Object items = body.get("items");

                        if (items instanceof Map) {
                            Map<String, Object> itemsMap = (Map<String, Object>) items;

                            if (itemsMap.containsKey("item")) {
                                List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                                for (Map<String, Object> item : itemList) {
                                    rowNumber = item.get("totalCnt").toString();

                                    if (!rowNumber.equals("0") || !rowNumber.isBlank()) {
                                        RequestList(contentTypeId, rowNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            log.info("===============================================================================");
            log.info("ContentTypeId: " + contentTypeId + " RowNumber: " + rowNumber);
            log.info("===============================================================================");
        }
    }

    private Map<String, Object> sendGetRequest(String urlString) throws java.io.IOException {
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

        if (response.charAt(0) != '{' && response.charAt(0) != '[') {
            log.info("===============================================================================");
            log.error("Invalid response format: " + response);
            log.info("===============================================================================");

            throw new RuntimeException("Invalid response format: " + response);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.toString(), Map.class);
    }

    private void RequestList(String contentTypeId, String rowNumber) throws java.io.IOException {
        String listUrl = FRONT_URL + "areaBasedList1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=" + rowNumber + "&pageNo=1" +
                "&listYN=Y&arrange=A&areaCode=39&sigunguCode=&contentTypeId=" + contentTypeId +
                "&cat1=&cat2=&cat3=";

        Map<String, Object> response = sendGetRequest(listUrl);
        if (response != null) {
            Map<String, Object> responseBody = (Map<String, Object>) response.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null) {

                    if (body.containsKey("items")) {
                        Object items = body.get("items");

                        if (items instanceof Map) {
                            Map<String, Object> itemsMap = (Map<String, Object>) items;

                            if (itemsMap.containsKey("item")) {
                                List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                                for (Map<String, Object> item : itemList) {
                                    String contentId = item.get("contentid").toString();
                                    String imageUrl = item.get("firstimage").toString();
                                    String title = item.get("title").toString();
                                    String address = item.get("addr1").toString();
                                    double longitude = Double.parseDouble(item.get("mapy").toString());
                                    double latitude = Double.parseDouble(item.get("mapx").toString());

                                    TourApiContentType tourApiContentType = tourApiContentTypeRepository.findByContentType(ContentType.fromContentTypeId(contentTypeId));
                                    Optional<TourApiSpot> tourApiSpot = tourApiSpotRepository.findByContentTypeId(contentId);
                                    if (tourApiSpot.isEmpty()) {
                                        TourApiSpot newTourApiSpot = TourApiSpot.builder()
                                                .contentType(tourApiContentType)
                                                .title(title)
                                                .address(address)
                                                .thumbnailUrl(imageUrl.isEmpty() ? "" : imageUrl)
                                                .longitude(longitude)
                                                .latitude(latitude)
                                                .build();

                                        tourApiSpotRepository.save(newTourApiSpot);
                                    }
                                }
                            }
                        } else {
                            log.warn("===============================================================================");
                            log.warn("Items is not a Map: {}", items);
                            log.warn("===============================================================================");
                            throw new CustomException(RetCode.RET_CODE99);
                        }
                    }
                }
            }
        } else {
            log.info("===============================================================================");
            log.info("Failed to get response...");
            log.info("===============================================================================");
            throw new CustomException(RetCode.RET_CODE99);
        }
    }
}

