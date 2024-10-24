package com.example.jejugudgo.global.exception.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)  // null 필드 제외
public record CommonApiResponse(
        String retCode,
        String retMessage,
        String alertMessage,
        Object data
) {
}
