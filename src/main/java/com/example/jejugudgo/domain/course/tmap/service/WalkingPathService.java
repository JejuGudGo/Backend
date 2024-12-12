package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.WalkingPathRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathCoordination;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.course.tmap.entity.SearchOption;
import com.example.jejugudgo.domain.course.tmap.entity.WalkingPath;
import com.example.jejugudgo.domain.course.tmap.repository.WalkingPathRepository;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.GenerateWalkingPathRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalkingPathService {
    private final WalkingPathRepository walkingPathRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final TMapRequestService tMapRequestService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void generateAndSaveWalkingPath(GenerateWalkingPathRequest request) {
        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCourseRepository.findById(request.userJejuGudgoCourseId())
                .orElseThrow(() -> new RuntimeException("해당 코스를 찾을 수 없습니다."));

        for (SearchOption searchOption : SearchOption.values()) {
            saveWalkingPath(request, userJejuGudgoCourse, searchOption);
        }
    }

    private void saveWalkingPath(GenerateWalkingPathRequest request, UserJejuGudgoCourse userJejuGudgoCourse, SearchOption searchOption) {
        SpotInfoRequest modifiedSpotInfoRequest = new SpotInfoRequest(
                searchOption.getSearchOptionId(),
                request.spotInfoRequest().spots()
        );

        WalkingPathResponse walkingPathResponse = tMapRequestService.create(modifiedSpotInfoRequest);

        Map<String, Object> lineDataMap = new HashMap<>();
        lineDataMap.put("type", walkingPathResponse.type());
        lineDataMap.put("totalTime", walkingPathResponse.totalTime());
        lineDataMap.put("totalDistance", walkingPathResponse.totalDistance());
        lineDataMap.put("walkingPathCoordinations", walkingPathResponse.walkingPathCoordinations());

        try {
            String lineDataJson = objectMapper.writeValueAsString(lineDataMap);

            WalkingPath walkingPath = WalkingPath.builder()
                    .searchOption(searchOption)
                    .userJejuGudgoCourse(userJejuGudgoCourse)
                    .lineData(lineDataJson)
                    .build();

            walkingPathRepository.save(walkingPath);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }

    @Transactional(readOnly = true)
    public WalkingPathResponse getWalkingPath(HttpServletRequest servletRequest, WalkingPathRequest request) {
        WalkingPath walkingPath = walkingPathRepository.findByUserJejuGudgoCourseIdAndSearchOption(
                request.userJejuGudgoCourseId(),
                SearchOption.valueOf(request.type())
        ).orElseThrow(() -> new RuntimeException("해당 경로를 찾을 수 없습니다."));

        return parseWalkingPathResponse(walkingPath.getLineData());
    }

    private WalkingPathResponse parseWalkingPathResponse(String lineData) {
        try {
            Map<String, Object> lineDataMap = objectMapper.readValue(lineData, Map.class);

            return new WalkingPathResponse(
                    (String) lineDataMap.get("type"),
                    (String) lineDataMap.get("totalTime"),
                    (String) lineDataMap.get("totalDistance"),
                    (List<WalkingPathCoordination>) lineDataMap.get("walkingPathCoordinations")
            );
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }
}
