package com.gudgo.jeju.domain.planner.query;


import com.gudgo.jeju.domain.oreum.dto.OreumResponseDto;
import com.gudgo.jeju.domain.oreum.entity.OreumData;
import com.gudgo.jeju.global.data.oruem.entity.QOreumData;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OreumDataQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OreumDataQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<OreumResponseDto> getOreums(Pageable pageable) {
        QOreumData qOreumData = QOreumData.oreumData;

        List<OreumData> oreumDataList = queryFactory
                .selectFrom(qOreumData)
                .fetch();

        List<OreumResponseDto> oreumResponseDtos = oreumDataList.stream()
                .map(oreumData ->
                        new OreumResponseDto(
                                oreumData.getId(),
                                oreumData.getTourApiCategory1(),
                                oreumData.getTitle(),
                                oreumData.getAddress(),
                                oreumData.getLatitude(),
                                oreumData.getLongitude(),
                                oreumData.getContent()
                        ))
                .toList();

        return PaginationUtil.listToPage(oreumResponseDtos, pageable);
    }

    public OreumResponseDto getOreum(Long id) {
        QOreumData qOreumData = QOreumData.oreumData;

        OreumData oreumData = queryFactory
                .selectFrom(qOreumData)
                .where(qOreumData.id.eq(id))
                .fetchOne();

        return new OreumResponseDto(
                oreumData.getId(),
                oreumData.getTourApiCategory1(),
                oreumData.getTitle(),
                oreumData.getAddress(),
                oreumData.getLatitude(),
                oreumData.getLongitude(),
                oreumData.getContent()
        );
    }
}
