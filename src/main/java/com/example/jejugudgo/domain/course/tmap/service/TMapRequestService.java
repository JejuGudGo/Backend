package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.TMapRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TMapRequestService {
    private final WalkingPathComponent walkingPathComponent;

    // 경로 생성
    public WalkingPathResponse create(SpotInfoRequest spotInfoRequest) {
        var sortedSpots = spotInfoRequest.spots().stream()
                .sorted(Comparator.comparing(SpotInfo::order))
                .collect(Collectors.toList());

        var startSpot = sortedSpots.get(0);
        var endSpot = sortedSpots.get(sortedSpots.size() - 1);

        List<String> waypointList = sortedSpots.stream()
                .filter(spot -> !spot.equals(startSpot) && !spot.equals(endSpot))
                .map(spot -> String.format("%f,%f", spot.longitude(), spot.latitude()))
                .collect(Collectors.toList());

        String passList = waypointList.stream()
                .limit(5)
                .collect(Collectors.joining("_"));

        String startTitle = UriUtils.encode(startSpot.title(), StandardCharsets.UTF_8);
        String endTitle = UriUtils.encode(endSpot.title(), StandardCharsets.UTF_8);

        TMapRequest tMapRequest = new TMapRequest(
                Long.parseLong(spotInfoRequest.searchOption()),
                startTitle,
                startSpot.longitude(),
                startSpot.latitude(),
                endTitle,
                endSpot.longitude(),
                endSpot.latitude(),
                waypointList.stream().collect(Collectors.joining("_"))
        );

        return walkingPathComponent.sendRequest(tMapRequest, passList, spotInfoRequest);
    }
}