package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.TMapRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathCoordination;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.course.tmap.entity.SearchOption;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TMapRequestService {
    private static final String TMAP_API_URL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

    @Value("${tmap.api.key}")
    private String TMAP_API_KEY;

    // 경로 생성 메소드
    public WalkingPathResponse create(SpotInfoRequest spotInfoRequest) {
        // 경유지 순서대로 정렬하여 출발지, 도착지 및 경유지를 식별
        var sortedSpots = spotInfoRequest.spots().stream()
                .sorted((a, b) -> a.order().compareTo(b.order()))
                .collect(Collectors.toList());

        // 출발지와 도착지 준비
        var startSpot = sortedSpots.get(0);
        var endSpot = sortedSpots.get(sortedSpots.size() - 1);

        // 경유지 준비 및 passList 구성
        List<String> waypointList = new ArrayList<>();
        String waypoints = sortedSpots.stream()
                .filter(spot -> !spot.equals(startSpot) && !spot.equals(endSpot))
                .map(spot -> {
                    waypointList.add(String.format("%f,%f", spot.longitude(), spot.latitude()));
                    return String.format("%f,%f", spot.longitude(), spot.latitude());
                })
                .collect(Collectors.joining("_"));

        // 최대 5개의 경유지를 포함하는 passList 준비
        String passList = waypointList.stream()
                .limit(5)
                .collect(Collectors.joining("_"));

        // URL에 포함될 타이틀 인코딩
        String startTitle = UriUtils.encode(startSpot.title(), StandardCharsets.UTF_8);
        String endTitle = UriUtils.encode(endSpot.title(), StandardCharsets.UTF_8);

        // TMap 요청 준비
        TMapRequest tMapRequest = new TMapRequest(
                Long.parseLong(spotInfoRequest.searchOption()),
                startTitle,
                startSpot.longitude(),
                startSpot.latitude(),
                endTitle,
                endSpot.longitude(),
                endSpot.latitude(),
                waypoints
        );

        // TMap 요청 전송
        return sendTMapRequest(tMapRequest, passList, spotInfoRequest);
    }

    // TMap API 요청 전송 및 응답 처리 메소드
    private WalkingPathResponse sendTMapRequest(TMapRequest tMapRequest, String passList, SpotInfoRequest spotInfoRequest) {
        try {
            // URL 구성
            URL url = new URL(TMAP_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("appKey", TMAP_API_KEY);
            conn.setDoOutput(true);

            // JSON 바디 준비 및 passList 포함
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

            // JSON 바디 전송
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            // 응답 읽기
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(conn.getInputStream());
            conn.disconnect();

            // 응답 파싱
            return parseWalkingPathResponse(responseNode.toString(), tMapRequest.searchOption(), spotInfoRequest);

        } catch (Exception e) {
            throw new RuntimeException("TMap API 요청 실패", e);
        }
    }

    // 응답 파싱 및 WalkingPathResponse 객체 생성 메소드
    private WalkingPathResponse parseWalkingPathResponse(String jsonResponse, Long searchOption, SpotInfoRequest spotInfoRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        // 총 시간 및 거리 추출
        int totalTimeSeconds = rootNode.path("features")
                .path(0)
                .path("properties")
                .path("totalTime").asInt();
        int totalDistanceMeters = rootNode.path("features")
                .path(0)
                .path("properties")
                .path("totalDistance").asInt();

        // searchOption을 이름으로 변환
        SearchOption searchOptionEnum = SearchOption.fromSearchOptionId(searchOption.toString());
        String searchOptionName = searchOptionEnum != null ? searchOptionEnum.getSearchOptionName() : null;

        // 총 시간을 "시간 분" 형식으로 변환
        int hours = totalTimeSeconds / 3600;
        int minutes = (totalTimeSeconds % 3600) / 60;
        String formattedTotalTime = (hours > 0 ? hours + "시간 " : "") + (minutes > 0 ? minutes + "분" : "");

        // 총 거리를 "km" 형식으로 변환
        double totalDistanceKm = totalDistanceMeters / 1000.0;
        String formattedTotalDistance = String.format("%.1fkm", totalDistanceKm);

        // 좌표 추출 및 목록 생성
        List<WalkingPathCoordination> coordinates = new ArrayList<>();
        JsonNode featuresNode = rootNode.path("features");
        var sortedSpots = spotInfoRequest.spots().stream()
                .sorted((a, b) -> a.order().compareTo(b.order()))
                .collect(Collectors.toList());

        int index = 0;  // Use index to map coordinates to spot titles based on order
        int order = 1;  // order 초기화

        for (JsonNode feature : featuresNode) {
            JsonNode geometryNode = feature.path("geometry");
            if ("Point".equals(geometryNode.path("type").asText())) {
                JsonNode coordinatesNode = geometryNode.path("coordinates");
                double longitude = coordinatesNode.get(0).asDouble();  // 경도
                double latitude = coordinatesNode.get(1).asDouble();  // 위도
                String pointType = feature.path("properties").path("pointType").asText();
                String title = null;  // 기본적으로 제목은 null로 설정

                // "SP", "PPn", "EP" 포인트 타입에 대해 제목 할당
                if (pointType.equals("SP") || pointType.startsWith("PP") || pointType.equals("EP")) {
                    if (index < sortedSpots.size()) {
                        title = sortedSpots.get(index).title();  // 현재 인덱스에 해당하는 제목 할당
                    }
                    index++;  // 제목이 할당된 포인트에 대해서만 인덱스 증가
                }

                coordinates.add(new WalkingPathCoordination(
                        title,  // 할당된 제목 또는 null
                        (long) order++,  // order 값을 1씩 증가
                        latitude,
                        longitude
                ));
            }
        }



        return new WalkingPathResponse(
                searchOptionName,  // searchOptionName으로 변환
                formattedTotalTime,  // "시간 분" 형식으로 변환
                formattedTotalDistance,  // "km" 형식으로 변환
                coordinates
        );
    }
}
