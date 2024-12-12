package com.example.jejugudgo.domain.course.tmap.service;

import com.example.jejugudgo.domain.course.tmap.dto.request.WalkingPathRequest;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathCoordination;
import com.example.jejugudgo.domain.course.tmap.dto.response.WalkingPathResponse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSearchOption;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import com.example.jejugudgo.domain.course.tmap.entity.WalkingPath;
import com.example.jejugudgo.domain.course.tmap.repository.WalkingPathRepository;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.GenerateWalkingPathRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.SpotInfoRequest;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoSearchOptionRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkingPathService {
    private final WalkingPathRepository walkingPathRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final TMapRequestService tMapRequestService;
    private final ObjectMapper objectMapper;
    private final UserJejuGudgoSearchOptionRepository userJejuGudgoSearchOptionRepository;

    @Async
    @Transactional
    public void generateAndSaveWalkingPath(GenerateWalkingPathRequest request) {
        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCourseRepository.findById(request.userJejuGudgoCourseId())
                .orElseThrow(EntityNotFoundException::new);

        SearchOption searchOption = SearchOption.fromSearchOptionId(request.spotInfoRequest().searchOption());


        UserJejuGudgoSearchOption searchOptionEntity = Optional.ofNullable(
                userJejuGudgoSearchOptionRepository.findBySearchOption(searchOption)
        ).orElseThrow(EntityNotFoundException::new);

        saveWalkingPath(request, userJejuGudgoCourse, searchOptionEntity);
    }

    private void saveWalkingPath(GenerateWalkingPathRequest request, UserJejuGudgoCourse userJejuGudgoCourse, UserJejuGudgoSearchOption searchOptionEntity) {
        SpotInfoRequest modifiedSpotInfoRequest = new SpotInfoRequest(
                searchOptionEntity.getSearchOption().getSearchOptionId(),
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
                    .searchOption(searchOptionEntity)
                    .userJejuGudgoCourse(userJejuGudgoCourse)
                    .lineData(lineDataJson)
                    .build();

            walkingPathRepository.save(walkingPath);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }
    }

    public WalkingPathResponse getWalkingPath(HttpServletRequest servletRequest, WalkingPathRequest request) {

        // SearchOption 변환
        SearchOption searchOption = SearchOption.fromSearchOptionName(request.type());
        if (searchOption == null) {
            throw new CustomException(RetCode.RET_CODE18);
        }

        // SearchOption 조회
        UserJejuGudgoSearchOption searchOptionEntity = Optional.ofNullable(
                userJejuGudgoSearchOptionRepository.findBySearchOption(searchOption)
        ).orElseThrow(() -> new CustomException(RetCode.RET_CODE97));


        // WalkingPath 조회
        WalkingPath walkingPath = walkingPathRepository
                .findByUserJejuGudgoCourseIdAndSearchOption(request.id(), searchOptionEntity)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        // 결과 반환
        return parseWalkingPathResponse(walkingPath.getLineData());
    }


    private WalkingPathResponse parseWalkingPathResponse(String lineData) {
        try {
            Map<String, Object> lineDataMap = objectMapper.readValue(lineData, Map.class);

            List<Map<String, Object>> rawCoordinations = (List<Map<String, Object>>) lineDataMap.get("walkingPathCoordinations");
            List<WalkingPathCoordination> walkingPathCoordinations = rawCoordinations.stream()
                    .map(coord -> new WalkingPathCoordination(
                            (String) coord.get("title"),
                            ((Number) coord.get("order")).longValue(),
                            ((Number) coord.get("latitude")).doubleValue(),
                            ((Number) coord.get("longitude")).doubleValue()
                    ))
                    .toList();

            return new WalkingPathResponse(
                    (String) lineDataMap.get("type"),
                    (String) lineDataMap.get("totalTime"),
                    (String) lineDataMap.get("totalDistance"),
                    walkingPathCoordinations
            );
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }

}
