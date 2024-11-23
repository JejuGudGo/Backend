package com.example.jejugudgo.domain.search.query;

import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchQueryService {
    private final JPAQueryFactory queryFactory;
    private final JejuOlleSearchQueryService olleTagSearchQueryService;
    private final JejuGudgoSearchQueryService jejuGudgoSearchQueryService;
    private final TrailSearchQueryService trailSearchQueryService;

    @Autowired
    public SearchQueryService(
            EntityManager entityManager, JejuOlleSearchQueryService olleTagSearchQueryService,
            JejuGudgoSearchQueryService jejuGudgoSearchQueryService, TrailSearchQueryService trailSearchQueryService
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.olleTagSearchQueryService = olleTagSearchQueryService;
        this.jejuGudgoSearchQueryService = jejuGudgoSearchQueryService;
        this.trailSearchQueryService = trailSearchQueryService;
    }

    public List<SearchListResponse> searchCoursesBySpotOrCategory(
            HttpServletRequest request, String category1, List<String> category2, List<String> category3,
            String latitude, String longitude, Pageable pageable
    ) {
        // 새로운 리스트를 매번 생성
        List<SearchListResponse> allResponses = new ArrayList<>();

        if (category1.equals("전체")) {
            List<SearchListResponse> olleCourses =
                    olleTagSearchQueryService.getJejuOlleCourses(request, category2, category3, latitude, longitude, Pageable.unpaged());

            List<SearchListResponse> gudgoCourses =
                    jejuGudgoSearchQueryService.getJejuGudgoCourses(request, category2, category3, latitude, longitude, Pageable.unpaged());

            List<SearchListResponse> trailCourses =
                    trailSearchQueryService.getTrails(request, category2, category3, latitude, longitude, Pageable.unpaged());

            allResponses.addAll(olleCourses);
            allResponses.addAll(gudgoCourses);
            allResponses.addAll(trailCourses);

            sortCourses(allResponses);

            return paginateResults(allResponses, pageable);

        } else if (category1.equals(BookmarkType.OLLE.getCode())) {
            return olleTagSearchQueryService.getJejuOlleCourses(request, category2, category3, latitude, longitude, pageable);

        } else if (category1.equals(BookmarkType.JEJU_GUDGO.getCode())) {
            return jejuGudgoSearchQueryService.getJejuGudgoCourses(request, category2, category3, latitude, longitude, pageable);

        } else if (category1.equals(BookmarkType.TRAIL.getCode())) {
            return trailSearchQueryService.getTrails(request, category2, category3, latitude, longitude, pageable);
        }

        return new ArrayList<>();
    }

    private void sortCourses(List<SearchListResponse> responses) {
        responses.sort((a, b) -> {
            if (a.id().equals(b.id())) {
                return a.title().compareToIgnoreCase(b.title());
            } else {
                return a.id().compareTo(b.id());
            }
        });
    }

    private List<SearchListResponse> paginateResults(List<SearchListResponse> responses, Pageable pageable) {
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), responses.size());
        if (start >= responses.size()) {
            return new ArrayList<>();
        }
        return responses.subList(start, end);
    }
}