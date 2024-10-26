package com.example.jejugudgo.global.api.tourapi.common.service;

import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiSpots;
import com.example.jejugudgo.global.api.tourapi.common.repository.TourApiContentTypeRepository;
import com.example.jejugudgo.global.api.tourapi.common.repository.TourApiSpotRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourApiDataRequestService {
    @Value("${tour.api.key}")
    private String KEY;
    private final String FRONT_URL = "http://apis.data.go.kr/B551011/KorService1/";

    private final TourApiSpotRepository tourApiSpotRepository;
    private final TourApiContentTypeRepository tourApiContentTypeRepository;

    public void loadTourApiData() throws IOException {
        List<TourApiContentType> contentTypes = tourApiContentTypeRepository.findAll();
        for (TourApiContentType contentType : contentTypes) {
            requestRowNumber(contentType.getContentType().getContentTypeId());
        }
    }

    private void requestRowNumber(String contentTypeId) throws IOException {
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
                                    String contentId = item.get("contentid").toString();
                                    String imageUrl = item.get("firstimage").toString();
                                    String title = item.get("title").toString();
                                    String address = item.get("addr1").toString();
                                    double longitude = Double.parseDouble(item.get("mapy").toString());
                                    double latitude = Double.parseDouble(item.get("mapx").toString());

                                    TourApiContentType tourApiContentType = tourApiContentTypeRepository.findByContentType(ContentType.fromContentTypeId(contentTypeId));
                                    Optional<TourApiSpots> tourApiSpot = tourApiSpotRepository.findByContentId(contentId);
                                    if (!tourApiSpot.isPresent()) {
                                        TourApiSpots newTourApiSpot = TourApiSpots.builder()
                                                .tourApiContentType(tourApiContentType)
                                                .title(title)
                                                .address(address)
                                                .contentId(contentId)
                                                .imageUrl(imageUrl.isEmpty() ? "" : imageUrl)
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

    private void requestSpotData(String contentTypeId, String contentId) throws IOException {
        TourApiSpots tourApiSpots = tourApiSpotRepository.findByContentId(contentId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        String summary = "";
        String phone = "";
        String homepage = "";
        String fee = "";
        String openingHours = "";
        String eventStartDate = "";
        String eventEndDate = "";
        String eventContent = "";
        String eventFee = "";
        String eventPlace = "";
        String sponsor = "";

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
                                if (item.containsKey("homepage")) {
                                    homepage = item.get("homepage").toString();
                                }

                                if (item.containsKey("overview")) {
                                    summary = item.get("overview").toString();
                                }
                            }
                        } else {
                            log.warn("Items is not a Map: {}", items);
                            throw new CustomException(RetCode.RET_CODE99);
                        }
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
            throw new CustomException(RetCode.RET_CODE99);
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
//                                if (item.containsKey("chkpet")) {
//                                    availablePet = item.get("chkpet").toString();
//                                }

                                if (item.containsKey("usetimeculture")) {
                                    openingHours = item.get("usetimeculture").toString();
                                }

                                if (item.containsKey("sponsor1")) {
                                    sponsor = item.get("sponsor1").toString();

                                    if (item.containsKey("sponsor1tel")) {
                                        phone = item.get("sponsor1tel").toString();
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
                        } else {
                            log.warn("Items is not a Map: {}", items);
                            throw new CustomException(RetCode.RET_CODE99);
                        }
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
            throw new CustomException(RetCode.RET_CODE99);
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
                                    if (item.get("infoname").toString().equals("행사소개")) {
                                        eventContent = item.get("infotext").toString();
                                    }
                                }
                            }
                        } else {
                            log.warn("Items is not a Map: {}", items);
                            throw new CustomException(RetCode.RET_CODE99);
                        }
                    }
                }
            }
        } else {
            log.error("Failed to get response from API");
            throw new CustomException(RetCode.RET_CODE99);
        }

        TourApiSpots updatedSpot = TourApiSpots.builder()
                .id(tourApiSpots.getId())
                .summary(summary.isEmpty() ? tourApiSpots.getSummary() : summary)
                .phone(phone.isEmpty() ? tourApiSpots.getPhone() : phone)
                .homepage(homepage.isEmpty() ? tourApiSpots.getHomepage() : homepage)
                .fee(fee.isEmpty() ? tourApiSpots.getFee() : fee)
                .openingHours(openingHours.isEmpty() ? tourApiSpots.getOpeningHours() : openingHours)
                .eventStartDate(eventStartDate.isEmpty() ? tourApiSpots.getEventStartDate() : eventStartDate)
                .eventEndDate(eventEndDate.isEmpty() ? tourApiSpots.getEventEndDate() : eventEndDate)
                .eventContent(eventContent.isEmpty() ? tourApiSpots.getEventContent() : eventContent)
                .eventFee(eventFee.isEmpty() ? tourApiSpots.getEventFee() : eventFee)
                .eventPlace(eventPlace.isEmpty() ? tourApiSpots.getEventPlace() : eventPlace)
                .sponsor(sponsor.isEmpty() ? tourApiSpots.getSponsor() : sponsor)
                .imageUrl(tourApiSpots.getImageUrl())
                .tourApiContentType(tourApiSpots.getTourApiContentType())
                .contentId(tourApiSpots.getContentId())
                .title(tourApiSpots.getTitle())
                .longitude(tourApiSpots.getLongitude())
                .latitude(tourApiSpots.getLatitude())
                .build();

        tourApiSpots = tourApiSpots.updateTourApiSpots(updatedSpot);
        tourApiSpotRepository.save(tourApiSpots);
    }
}
