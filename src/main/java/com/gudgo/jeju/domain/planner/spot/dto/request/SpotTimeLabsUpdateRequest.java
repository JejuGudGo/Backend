package com.gudgo.jeju.domain.planner.spot.dto.request;

import java.time.LocalTime;

public record SpotTimeLabsUpdateRequest(
        LocalTime time
) {
}
