package com.example.jejugudgo.global.exception.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonApiResponse(
        String retCode,
        String retMessage,
        String alertMessage,
        @JsonInclude(JsonInclude.Include.NON_NULL) Object data
) {
    public static CommonApiResponse of(String retCode, String retMessage, String alertMessage, Object data, String listKey) {
        if (data instanceof List<?>) {
            Map<String, Object> wrappedData = new HashMap<>();
            wrappedData.put(listKey, data);
            return new CommonApiResponse(retCode, retMessage, alertMessage, wrappedData);
        }
        return new CommonApiResponse(retCode, retMessage, alertMessage, data);
    }
}
