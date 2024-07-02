package com.gudgo.jeju.global.data.oruem;


import com.gudgo.jeju.domain.planner.query.OreumDataQueryService;
import com.gudgo.jeju.global.data.oruem.dto.OreumResponseDto;
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

    private final OreumDataQueryService oreumDataQueryService;

    /* GET : 전체 오름 조회 */
    @GetMapping(value = "")
    public Page<OreumResponseDto> getOreums(Pageable pageable) {
        return oreumDataQueryService.getOreums(pageable);
    }

    /* GET : 오름 상세 조회 */
    @GetMapping(value = "/{id}")
    public OreumResponseDto getOreum(@PathVariable("id") Long id) {
        return oreumDataQueryService.getOreum(id);
    }
}
