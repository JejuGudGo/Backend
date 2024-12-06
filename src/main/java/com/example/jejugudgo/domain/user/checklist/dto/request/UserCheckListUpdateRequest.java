package com.example.jejugudgo.domain.user.checklist.dto.request;

import jakarta.annotation.Nullable;

public record UserCheckListUpdateRequest(
        @Nullable String content,
        @Nullable Boolean isFinished,
        @Nullable Long order
) {
}
