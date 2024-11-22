package com.example.jejugudgo.domain.search.service;

import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
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
        if (type.equals(BookmarkType.JEJU_GUDGO.getCode()))
            return jejuGudgoSearchDetailService.getJejuGudgoCourseDetail(request, id);

        else if (type.equals(BookmarkType.OLLE.getCode()))
            return jejuOlleSearchDetailService.getJejuOlleCourseDetail(request, id);

        else if (type.equals(BookmarkType.TRAIL.getCode()))
            return trailSearchDetailService.getTrailDetail(request, id);

        return null;
    }
}
