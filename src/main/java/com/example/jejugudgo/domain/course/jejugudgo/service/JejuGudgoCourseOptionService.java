package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseOptionResponse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseOption;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseOptionService {

    private final JejuGudgoCourseOptionRepository jejuGudgoCourseOptionRepository;

    public List<JejuGudgoCourseOptionResponse> getOptions(Long courseId) {
        // 해당 courseId에 연결된 JejuGudgoCourseOption을 조회
        List<JejuGudgoCourseOption> options = jejuGudgoCourseOptionRepository.findByJejuGudgoCourseId(courseId);

        // 조회된 옵션들을 JejuGudgoCourseOptionResponse 형태로 변환
        return options.stream()
                .map(option -> new JejuGudgoCourseOptionResponse(
                        option.getWalkingType().getType(), // 걷기 타입
                        option.getTime(),                 // 걷기 소요 시간
                        option.getDistance()              // 걷기 거리
                ))
                .toList();
    }

}
