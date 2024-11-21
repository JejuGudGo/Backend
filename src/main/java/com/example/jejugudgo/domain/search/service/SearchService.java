package com.example.jejugudgo.domain.search.service;

import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final JejuGudgoSearchDetailService jejuGudgoSearchDetailService;
    private final JejuOlleSearchDetailService jejuOlleSearchDetailService;
    private final TrailSearchDetailService trailSearchDetailService;

    public SearchDetailResponse getCourseDetail(HttpServletRequest request, String type, Long id) {
        if (type.equals("제주걷고"))
            return jejuGudgoSearchDetailService.getJejuGudgoCourseDetail(request, id);

        else if (type.equals("제주올레"))
            return jejuOlleSearchDetailService.getJejuOlleCourseDetail(request, id);

        else if (type.equals("산책로"))
            return trailSearchDetailService.getTrailDetail(request, id);

        return null;
    }
}
