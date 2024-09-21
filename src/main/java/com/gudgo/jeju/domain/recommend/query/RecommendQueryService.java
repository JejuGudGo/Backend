package com.gudgo.jeju.domain.recommend.query;


import com.gudgo.jeju.domain.recommend.dto.response.RecommendResponse;
import com.gudgo.jeju.domain.recommend.entity.QRecommend;
import com.gudgo.jeju.domain.recommend.entity.Recommend;
import com.gudgo.jeju.domain.recommend.repository.RecommendRepository;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    RecommendQueryService(EntityManager entityManager, RecommendRepository recommendRepository){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<RecommendResponse> getRecommends(Pageable pageable) {
        QRecommend recommend = QRecommend.recommend;

        List<Recommend> recommendList = queryFactory
                .selectFrom(recommend)
                .fetch();

        List<RecommendResponse> recommendResponses = recommendList.stream().map(r -> {
            return new RecommendResponse(
                    r.getId(),
                    r.getTitle1(),
                    r.getAuthor(),
                    r.getCreatedAt(),
                    r.getTitle2(),
                    r.getSection1Title(),
                    r.getSection2Title(),
                    r.getSection3Title(),
                    r.getSection4Title(),
                    r.getSection1Content(),
                    r.getSection2Content(),
                    r.getSection3Content(),
                    r.getSection4Content()
            );
        }).collect(Collectors.toList());

        return PaginationUtil.listToPage(recommendResponses, pageable);
    }



}
