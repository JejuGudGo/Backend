package com.gudgo.jeju.domain.recommend.service;

import com.gudgo.jeju.domain.recommend.dto.response.RecommendResponse;
import com.gudgo.jeju.domain.recommend.entity.Recommend;
import com.gudgo.jeju.domain.recommend.repository.RecommendRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendService {
    private final RecommendRepository recommendRepository;

    public RecommendResponse getRecommend(Long recommendId) {
        Recommend recommend = recommendRepository.findById(recommendId)
                .orElseThrow(EntityNotFoundException::new);

        return new RecommendResponse(
                recommend.getId(),
                recommend.getTitle1(),
                recommend.getAuthor(),
                recommend.getCreatedAt(),
                recommend.getTitle2(),
                recommend.getSection1Title(),
                recommend.getSection2Title(),
                recommend.getSection3Title(),
                recommend.getSection4Title(),
                recommend.getSection1Content(),
                recommend.getSection2Content(),
                recommend.getSection3Content(),
                recommend.getSection4Content()
        );

    }
}
