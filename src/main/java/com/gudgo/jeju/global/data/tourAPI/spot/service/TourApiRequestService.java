package com.gudgo.jeju.global.data.tourAPI.spot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.global.data.cache.TourApiSpotCachingService;
import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiCategory3;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiContentType;
import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
import com.gudgo.jeju.global.data.tourAPI.common.repository.DataConfigurationRepository;
import com.gudgo.jeju.global.data.tourAPI.common.repository.TourApiCategory3Repository;
import com.gudgo.jeju.global.data.tourAPI.common.repository.TourApiContentTypeRepository;
import com.gudgo.jeju.global.data.tourAPI.common.repository.TourApiSubContentTypeRepository;
import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotData;
import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotImage;
import com.gudgo.jeju.global.data.tourAPI.spot.repository.TourApiSpotDataRepository;
import com.gudgo.jeju.global.data.tourAPI.spot.repository.TourApiImageRepository;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourApiRequestService {

    @Value("${tour.api.key}")
    private String KEY;

    private final TourApiContentTypeRepository  tourApiContentTypeRepository;
    private final TourApiCategory3Repository tourApiCategory3Repository;
    private final TourApiSubContentTypeRepository tourApiSubContentTypeRepository;
    private final TourApiSpotDataRepository tourApiSpotDataRepository;
    private final TourApiImageRepository tourApiImageRepository;
    private final DataConfigurationRepository dataConfigurationRepository;
    private final TourApiSpotCachingService tourApiSpotCachingService;

    private final String FRONT_URL = "http://apis.data.go.kr/B551011/KorService1/";

    public void prepareTourApiData() throws IOException {
        log.info("===============================================================================");
        log.info("Starting to prepare Tour API CSV data...");
        log.info("===============================================================================");

        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("TourSpotCommon")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            List<TourApiContentType> tourApiContentTypes = tourApiContentTypeRepository.findAll();

            for (TourApiContentType tourApiContentType : tourApiContentTypes) {
                String id = tourApiContentType.getId();
                RequestRowNumber(id);
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("TourSpotCommon")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);

            } else if (!checkDataConfig.isConfigValue()){
                checkDataConfig.setConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);
            }

        } else {
            log.info("===============================================================================");
            log.info("TourApiCommonCsvData is already loaded");
            log.info("===============================================================================");
        }
    }

    private void RequestRowNumber(String contentTypeId) throws IOException {
        String requestUrl = FRONT_URL + "areaBasedList1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=" + "&pageNo=1" +
                "&listYN=N&arrange=A&areaCode=39&sigunguCode=&contentTypeId=" + contentTypeId +
                "&cat1=&cat2=&cat3=";

        Map<String, Object> response = sendGetRequest(requestUrl);
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
                                    String rowNumber = item.get("totalCnt").toString();

                                    if (!rowNumber.equals("0") || !rowNumber.isBlank()) {
                                        RequestList(contentTypeId, rowNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void RequestList(String contentTypeId, String rowNumber) throws IOException {
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
                                    String cat3 = item.get("cat3").toString();
                                    String contentId = item.get("contentid").toString();
                                    String longitude = item.get("mapy").toString();
                                    String latitude = item.get("mapx").toString();
                                    String updateTime = item.get("modifiedtime").toString();

                                    Optional<TourApiCategory3> tourApiCategory3 = tourApiCategory3Repository.findById(cat3);

                                    if (tourApiCategory3.isPresent()) {
                                        TourApiSubContentType tourApiSubContentType = TourApiSubContentType.builder()
                                                .tourApiCategory3(tourApiCategory3.get())
                                                .id(contentId)
                                                .longitude(longitude)
                                                .latitude(latitude)
                                                .updatedAt(updateTime)
                                                .build();

                                        tourApiSubContentTypeRepository.save(tourApiSubContentType);
                                    }
                                }
                            }
                        } else {
                            log.warn("===============================================================================");
                            log.warn("Items is not a Map: {}", items);
                            log.warn("===============================================================================");
                        }
                    }
                }
            }
        } else {
            log.info("===============================================================================");
            log.info("Failed to get response...");
            log.info("===============================================================================");
        }
    }

    @Transactional
    public void requestSpotDetail(String contentId, String contentTypeId) throws IOException {
        List<String> images = new ArrayList<>();
        TourApiSubContentType tourApiSubContentType = tourApiSubContentTypeRepository.findById(contentId).get();

        String title = "";
        String address = "";
        String content = "";
        String pageUrl = "";
        String info = "";
        String closeDay = "";
        String organizerInfo = "";
        String organizeNumber = "";
        String eventStartDate = "";
        String eventEndDate = "";
        String fee = "";
        String time = "";
        String park = "";
        String rentStroller = "";
        String availablePet = "";
        String eventContent = "";
        String eventFee = "";
        String eventPlace = "";
        String guideService = "";
        String toilet = "";
        String reserveInfo = "";

        // 공통 정보
        String commonUrl = FRONT_URL + "detailCommon1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=" + contentTypeId +
                "&contentId=" + contentId + "&defaultYN=Y&firstImageYN=N&areacodeYN=N&catcodeYN=N" +
                "&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";

        // 소개 정보
        String introUrl = FRONT_URL + "detailIntro1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=" + contentTypeId +
                "&contentId=" + contentId;

        // 반복 정보
        String infoUrl = FRONT_URL + "detailInfo1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=" + contentTypeId +
                "&contentId=" + contentId;

        // 추가 이미지 정보
        String imageUrl = FRONT_URL + "detailImage1?ServiceKey=" + KEY +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&imageYN=Y&subImageYN=Y&numOfRows=10" +
                "&contentId=" + contentId;

        // 공통 정보
        Map<String, Object> commonResponse = sendGetRequest(commonUrl);
        if (commonResponse != null) {
            Map<String, Object> responseBody = (Map<String, Object>) commonResponse.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null && body.containsKey("items")) {
                    Object items = body.get("items");

                    if (items instanceof Map) {
                        Map<String, Object> itemsMap = (Map<String, Object>) items;

                        if (itemsMap.containsKey("item")) {
                            List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                            for (Map<String, Object> item : itemList) {
                                if (item.containsKey("title")) {
                                    title = item.get("title").toString();
                                }

                                if (item.containsKey("homepage")) {
                                    pageUrl = item.get("homepage").toString();
                                }

                                if (item.containsKey("addr1")) {
                                    address = item.get("addr1").toString();
                                }

                                if (item.containsKey("overview")) {
                                    content = item.get("overview").toString();
                                }
                            }
                        }
                    } else {
                        log.warn("Items is not a Map: {}", items);
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
        }

        // 소개정보
        Map<String, Object> introResponse = sendGetRequest(introUrl);
        if (introResponse != null) {
            Map<String, Object> responseBody = (Map<String, Object>) introResponse.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null && body.containsKey("items")) {
                    Object items = body.get("items");

                    if (items instanceof Map) {
                        Map<String, Object> itemsMap = (Map<String, Object>) items;

                        if (itemsMap.containsKey("item")) {
                            List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                            for (Map<String, Object> item : itemList) {
                                if (item.containsKey("restdate")) {
                                    closeDay = item.get("restdate").toString();
                                }

                                if (item.containsKey("infocenter")) {
                                    info = item.get("infocenter").toString();
                                }

                                if (item.containsKey("parking")) {
                                    park = item.get("parking").toString();
                                }

                                if (item.containsKey("chkpet")) {
                                    availablePet = item.get("chkpet").toString();
                                }

                                if (item.containsKey("chkbabycarriage")) {
                                    rentStroller = item.get("chkbabycarriage").toString();
                                }

                                if (item.containsKey("reservationfood")) {
                                    reserveInfo = item.get("reservationfood").toString();
                                }

                                if (item.containsKey("usetimeculture")) {
                                    time = item.get("usetimeculture").toString();
                                }

                                if (item.containsKey("sponsor1")) {
                                    organizerInfo = item.get("sponsor1").toString();

                                    if (item.containsKey("sponsor1tel")) {
                                        organizeNumber = item.get("sponsor1tel").toString();
                                    }
                                }

                                if (item.containsKey("eventstartdate")) {
                                    eventStartDate = item.get("eventstartdate").toString();
                                    eventEndDate = item.get("eventenddate").toString();
                                }

                                if (item.containsKey("usetimefestival")) {
                                    eventFee = item.get("usetimefestival").toString();
                                }

                                if (item.containsKey("eventplace")) {
                                    eventPlace = item.get("eventplace").toString();
                                }
                            }
                        }
                    } else {
                        log.warn("Items is not a Map: {}", items);
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
        }

        // 반복정보
        Map<String, Object> infoResponse = sendGetRequest(infoUrl);
        if (infoResponse != null) {
            Map<String, Object> responseBody = (Map<String, Object>) infoResponse.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null && body.containsKey("items")) {
                    Object items = body.get("items");

                    if (items instanceof Map) {
                        Map<String, Object> itemsMap = (Map<String, Object>) items;

                        if (itemsMap.containsKey("item")) {
                            List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                            for (Map<String, Object> item : itemList) {
                                if (item.containsKey("infoname")) {
                                    if (item.get("infoname").toString().equals("입 장 료") || item.get("infoname").toString().equals("시설이용료")) {
                                        fee = item.get("infotext").toString();
                                    }
                                }

                                if (item.containsKey("infoname")) {
                                    if (item.get("infoname").toString().equals("화장실")) {
                                        toilet = item.get("infotext").toString();
                                    }
                                }

                                if (item.containsKey("infoname")) {
                                    if (item.get("infoname").toString().equals("행사소개")) {
                                        eventContent = item.get("infotext").toString();
                                    }
                                }
                                if (item.containsKey("infoname")) {
                                    if (item.get("infoname").toString().equals("외국어 안내서비스")) {
                                        guideService = item.get("infotext").toString();
                                    }
                                }
                            }
                        }
                    } else {
                        log.warn("Items is not a Map: {}", items);
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
        }

        // 추가 이미지
        Map<String, Object> imageResponse = sendGetRequest(imageUrl);
        if (imageResponse != null) {
            Map<String, Object> responseBody = (Map<String, Object>) imageResponse.get("response");

            if (responseBody != null) {
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");

                if (body != null && body.containsKey("items")) {
                    Object items = body.get("items");

                    if (items instanceof Map) {
                        Map<String, Object> itemsMap = (Map<String, Object>) items;

                        if (itemsMap.containsKey("item")) {
                            List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");

                            for (Map<String, Object> item : itemList) {
                                if (item.containsKey("originimgurl")) {
                                    String url = item.get("originimgurl").toString();
                                    images.add(url);
                                }
                            }
                        }
                    } else {
                        log.warn("Items is not a Map: {}", items);
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
        }

        TourApiSpotData tourApiSpotData = TourApiSpotData.builder()
                .title(title)
                .address(address)
                .content(content)
                .availablePet(availablePet)
                .pageUrl(pageUrl)
                .park(park)
                .fee(fee)
                .park(park)
                .toilet(toilet)
                .closeDay(closeDay)
                .rentStroller(rentStroller)
                .info(info)
                .time(time)
                .reserveInfo(reserveInfo)
                .organizerInfo(organizerInfo)
                .organizeNumber(organizeNumber)
                .eventStartDate(eventStartDate)
                .eventEndDate(eventEndDate)
                .eventContent(eventContent)
                .eventFee(eventFee)
                .eventPlace(eventPlace)
                .guideService(guideService)
                .build();

        tourApiSpotDataRepository.save(tourApiSpotData);

        tourApiSubContentType.setTourApiSpotData(tourApiSpotData);
        tourApiSubContentTypeRepository.save(tourApiSubContentType);

        log.info("===============================================================================");
        log.info("All TourApiSpot Info saved!");
        log.info("===============================================================================");

        for (String image : images) {
            TourApiSpotImage tourApiSpotImage = TourApiSpotImage.builder()
                    .tourApiSpotData(tourApiSpotData)
                    .imageUrl(image)
                    .build();

            tourApiImageRepository.save(tourApiSpotImage);
        }

        log.info("===============================================================================");
        log.info("All TourApiSpot Images saved!");
        log.info("===============================================================================");

        tourApiSpotCachingService.setSpotData(contentId);

        log.info("===============================================================================");
        log.info("TourApiSpot info cached!");
        log.info("===============================================================================");

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

        if (response.charAt(0) != '{' && response.charAt(0) != '[') {
            log.info("===============================================================================");
            log.error("Invalid response format: " + response);
            log.info("===============================================================================");

            throw new RuntimeException("Invalid response format: " + response);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.toString(), Map.class);
    }
}
