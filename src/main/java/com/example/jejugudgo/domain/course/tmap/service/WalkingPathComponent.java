package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.TMapRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathCoordination;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalkingPathComponent {
    private static final String TMAP_API_URL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

    @Value("${tmap.api.key}")
    private String TMAP_API_KEY;

    // TMap API 요청 및 응답 처리
    public WalkingPathResponse sendRequest(TMapRequest tMapRequest, String passList, SpotInfoRequest spotInfoRequest) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(TMAP_API_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("appKey", TMAP_API_KEY);
            conn.setDoOutput(true);

            String jsonBody = String.format(
                    "{\"searchOption\":%d,\"startX\":%f,\"startY\":%f,\"endX\":%f,\"endY\":%f,\"startName\":\"%s\",\"endName\":\"%s\",\"passList\":\"%s\"}",
                    tMapRequest.searchOption(),
                    tMapRequest.startX(),
                    tMapRequest.startY(),
                    tMapRequest.endX(),
                    tMapRequest.endY(),
                    tMapRequest.startName(),
                    tMapRequest.endName(),
                    passList
            );

            // 요청 JSON 로깅
            log.info("Sending TMap API request: " + jsonBody);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            // 응답 처리
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return handleErrorResponse(conn.getErrorStream());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(conn.getInputStream());


            return parseResponse(responseNode.toString(), tMapRequest.searchOption(), spotInfoRequest);
        } catch (Exception e) {
            throw new RuntimeException("TMap API 요청 실패", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // 오류 응답 처리 및 로깅
    private WalkingPathResponse handleErrorResponse(InputStream errorStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode errorNode = mapper.readTree(errorStream);
        String errorDetail = errorNode.path("error").toString();
        // 오류 응답 로깅
        log.info("TMap API error response: " + errorDetail);
        throw new RuntimeException("TMap API 요청 오류: " + errorDetail);
    }
    // 응답 파싱
    private WalkingPathResponse parseResponse(String jsonResponse, Long searchOption, SpotInfoRequest spotInfoRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        // 시간 및 거리 계산
        JsonNode firstFeature = rootNode.path("features").path(0);
        int totalTimeSeconds = firstFeature.path("properties").path("totalTime").asInt();
        int totalDistanceMeters = firstFeature.path("properties").path("totalDistance").asInt();

        // 좌표 처리
        List<WalkingPathCoordination> coordinates = new ArrayList<>();
        List<SpotInfo> sortedSpots = spotInfoRequest.spots().stream()
                .sorted(Comparator.comparing(SpotInfo::order))
                .collect(Collectors.toList());

        int spotIndex = 0;
        int order = 1;

        for (JsonNode feature : rootNode.path("features")) {
            JsonNode geometryNode = feature.path("geometry");
            String geoType = geometryNode.path("type").asText();
            JsonNode coordinatesNode = geometryNode.path("coordinates");
            String pointType = feature.path("properties").path("pointType").asText();

            String title = determineTitle(pointType, spotIndex, sortedSpots);
            if (title != null) {
                spotIndex++;
            }

            coordinates.addAll(processCoordinates(geoType, coordinatesNode, title, order));
            order += geoType.equals("Point") ? 1 : coordinatesNode.size();
        }

        // 값 변환 메서드 호출
        String searchOptionName = getSearchOptionName(searchOption);
        String formattedTotalTime = formatTotalTime(totalTimeSeconds);
        String formattedTotalDistance = formatTotalDistance(totalDistanceMeters);

        return new WalkingPathResponse(
                searchOptionName,
                formattedTotalTime,
                formattedTotalDistance,
                coordinates);
    }

    private String determineTitle(String pointType, int spotIndex, List<SpotInfo> sortedSpots) {
        return (pointType.equals("SP") || pointType.startsWith("PP") || pointType.equals("EP"))
                && spotIndex < sortedSpots.size()
                ? sortedSpots.get(spotIndex).title()
                : null;
    }

    private List<WalkingPathCoordination> processCoordinates(String geoType, JsonNode coordinatesNode, String title, int order) {
        List<WalkingPathCoordination> coordinates = new ArrayList<>();

        if (geoType.equals("Point")) {
            coordinates.add(new WalkingPathCoordination(
                    title, (long) order, coordinatesNode.get(1).asDouble(), coordinatesNode.get(0).asDouble()
            ));
        } else if (geoType.equals("LineString")) {
            for (JsonNode coordNode : coordinatesNode) {
                coordinates.add(new WalkingPathCoordination(
                        null, (long) order++, coordNode.get(1).asDouble(), coordNode.get(0).asDouble()
                ));
            }
        }

        return coordinates;
    }

    // 검색 옵션 이름 변환 메서드
    private String getSearchOptionName(Long searchOption) {
        SearchOption searchOptionEnum = SearchOption.fromSearchOptionId(searchOption.toString());
        return searchOptionEnum != null ? searchOptionEnum.getSearchOptionName() : null;
    }

    // 시간 형식 변환 메서드
    private String formatTotalTime(int totalTimeSeconds) {
        int hours = totalTimeSeconds / 3600;
        int minutes = (totalTimeSeconds % 3600) / 60;
        return (hours > 0 ? hours + "시간 " : "") + (minutes > 0 ? minutes + "분" : "");
    }

    // 거리 형식 변환 메서드
    private String formatTotalDistance(int totalDistanceMeters) {
        return String.format("%.1fkm", totalDistanceMeters / 1000.0);
    }
}