package com.example.jejugudgo.global.exception;

import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
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
