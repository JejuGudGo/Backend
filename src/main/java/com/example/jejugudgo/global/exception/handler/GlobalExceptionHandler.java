package com.example.jejugudgo.global.exception.handler;

import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseUtil apiResponseUtil;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonApiResponse> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponseUtil.error(e.getRetCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponseUtil.error(RetCode.RET_CODE99));
    }
}
