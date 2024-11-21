package com.example.jejugudgo.global.exception.util;

import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiResponseUtil {

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
        return new CommonApiResponse(
                retCode.getRetCode(),
                retCode.getMessage(),
                null,
                null
        );
    }

    public CommonApiResponse error(RetCode retCode, String message) {
        return new CommonApiResponse(
                retCode.getRetCode(),
                message,
                null,
                null
        );
    }
}
