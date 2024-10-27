package com.example.jejugudgo.global.util;

import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.exception.repository.ApiResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiResponseUtil {

    private final ApiResponseRepository apiResponseRepository;

    // 성공 응답 생성
    public CommonApiResponse success(Object data) {
        return new CommonApiResponse(
                RetCode.RET_CODE00.getRetCode(),
                RetCode.RET_CODE00.getMessage(),
                null,
                data
        );
    }

    public CommonApiResponse success(Object data, String listKey) {
        return CommonApiResponse.of(
                RetCode.RET_CODE00.getRetCode(),
                RetCode.RET_CODE00.getMessage(),
                null,
                data,
                listKey
        );
    }

    // 오류 응답 생성
    public CommonApiResponse error(RetCode retCode) {
        // ApiResponseRepository에서 alertMessage를 조회
        String alertMessage = apiResponseRepository.findByRetCode(retCode.getRetCode()).getAlertMessage();

        // alertMessage가 빈 문자열이면 null로 설정
        if (alertMessage != null && alertMessage.isBlank()) {
            alertMessage = null;
        }

        return new CommonApiResponse(
                retCode.getRetCode(),
                retCode.getMessage(),
                alertMessage,
                null
        );
    }
}
