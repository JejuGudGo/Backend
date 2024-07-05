package com.gudgo.jeju.domain.oreum.query;


import com.gudgo.jeju.domain.oreum.dto.OreumResponseDto;
import com.gudgo.jeju.domain.oreum.entity.Oreum;
import com.gudgo.jeju.domain.oreum.entity.QOreum;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OreumSpotQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OreumSpotQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<OreumResponseDto> getOreums(Pageable pageable) {
        QOreum qOreum = QOreum.oreum;

        List<Oreum> oreumList = queryFactory
                .selectFrom(qOreum)
                .fetch();

        List<OreumResponseDto> oreumResponseDtos = oreumList.stream()
                .map(oreum ->
                        new OreumResponseDto(
                                oreum.getId(),
                                oreum.getTourApiCategory1(),
                                oreum.getTitle(),
                                oreum.getAddress(),
                                oreum.getLatitude(),
                                oreum.getLongitude(),
                                oreum.getContent()
                        ))
                .toList();

        return PaginationUtil.listToPage(oreumResponseDtos, pageable);
    }

    public OreumResponseDto getOreum(Long id) {
        QOreum qOreum = QOreum.oreum;

        Oreum oreum = queryFactory
                .selectFrom(qOreum)
                .where(qOreum.id.eq(id))
                .fetchOne();

        return new OreumResponseDto(
                oreum.getId(),
                oreum.getTourApiCategory1(),
                oreum.getTitle(),
                oreum.getAddress(),
                oreum.getLatitude(),
                oreum.getLongitude(),
                oreum.getContent()
        );
    }
}
