package com.gudgo.jeju.domain.recommend.controller;


import com.gudgo.jeju.domain.recommend.dto.response.RecommendResponse;
import com.gudgo.jeju.domain.recommend.query.RecommendQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
@Slf4j
@RestController
public class RecommendController {

    private final RecommendQueryService recommendQueryService;

    @GetMapping("")
    public Page<RecommendResponse> getRecommends(Pageable pageable) {
        return recommendQueryService.getRecommends(pageable);
    }
}
