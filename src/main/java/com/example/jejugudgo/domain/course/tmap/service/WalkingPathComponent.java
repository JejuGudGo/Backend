package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.TMapRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathCoordination;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.course.tmap.entity.SearchOption;
import org.springframework.stereotype.Component;

import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WalkingPathComponent {
    private static final String TMAP_API_URL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

    @Value("${tmap.api.key}")
    private String TMAP_API_KEY;

    // TMap API 요청 및 응답 처리
    public WalkingPathResponse sendRequest(TMapRequest tMapRequest, String passList, SpotInfoRequest spotInfoRequest) {
        try {
            // URL 연결 설정
            URL url = new URL(TMAP_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("appKey", TMAP_API_KEY);
            conn.setDoOutput(true);

            // 요청 바디 생성 및 전송
            String jsonBody = String.format(
                    "{\"startX\":%f,\"startY\":%f,\"endX\":%f,\"endY\":%f,\"startName\":\"%s\",\"endName\":\"%s\",\"passList\":\"%s\"}",
                    tMapRequest.startX(),
                    tMapRequest.startY(),
                    tMapRequest.endX(),
                    tMapRequest.endY(),
                    tMapRequest.startName(),
                    tMapRequest.endName(),
                    passList
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            // 응답 처리
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(conn.getInputStream());
            conn.disconnect();

            return parseResponse(responseNode.toString(), tMapRequest.searchOption(), spotInfoRequest);

        } catch (Exception e) {
            throw new RuntimeException("TMap API 요청 실패", e);
        }
    }

    // 응답 파싱
    private WalkingPathResponse parseResponse(String jsonResponse, Long searchOption, SpotInfoRequest spotInfoRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        // 시간 및 거리 계산
        int totalTimeSeconds = rootNode.path("features").path(0).path("properties").path("totalTime").asInt();
        int totalDistanceMeters = rootNode.path("features").path(0).path("properties").path("totalDistance").asInt();

        // 좌표 처리
        List<WalkingPathCoordination> coordinates = new ArrayList<>();
        JsonNode featuresNode = rootNode.path("features");
        List<SpotInfo> sortedSpots = spotInfoRequest.spots().stream()
                .sorted((a, b) -> a.order().compareTo(b.order()))
                .collect(Collectors.toList());

        int index = 0;
        int order = 1;

        for (JsonNode feature : featuresNode) {
            JsonNode geometryNode = feature.path("geometry");
            String geoType = geometryNode.path("type").asText();
            JsonNode coordinatesNode = geometryNode.path("coordinates");
            String pointType = feature.path("properties").path("pointType").asText();
            String title = null;

            if (pointType.equals("SP") || pointType.startsWith("PP") || pointType.equals("EP")) {
                if (index < sortedSpots.size()) {
                    title = sortedSpots.get(index).title();
                }
                index++;
            }

            if (geoType.equals("Point")) {
                coordinates.add(new WalkingPathCoordination(
                        title, (long) order++, coordinatesNode.get(1).asDouble(), coordinatesNode.get(0).asDouble()
                ));
            } else if (geoType.equals("LineString")) {
                for (JsonNode coordNode : coordinatesNode) {
                    coordinates.add(new WalkingPathCoordination(
                            null, (long) order++, coordNode.get(1).asDouble(), coordNode.get(0).asDouble()
                    ));
                }
            }
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
