package com.gudgo.jeju.domain.oreum.controller;


import com.gudgo.jeju.domain.oreum.query.OreumSpotQueryService;
import com.gudgo.jeju.domain.oreum.dto.OreumResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value="/api/v1/oreum")
@RequiredArgsConstructor
@Slf4j
@RestController
public class OreumController {

    private final OreumSpotQueryService oreumSpotQueryService;

    /* GET : 전체 오름 조회 */
    @GetMapping(value = "")
    public Page<OreumResponseDto> getOreums(Pageable pageable) {
        return oreumSpotQueryService.getOreums(pageable);
    }

    /* GET : 오름 상세 조회 */
    @GetMapping(value = "/{oreumId}")
    public OreumResponseDto getOreum(@PathVariable("oreumId") Long oreumId) {
        return oreumSpotQueryService.getOreum(oreumId);
    }
}
