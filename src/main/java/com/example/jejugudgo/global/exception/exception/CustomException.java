package com.example.jejugudgo.global.exception.exception;

import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final RetCode retCode;

    public CustomException(RetCode retCode) {
        super(retCode.getMessage());
        this.retCode = retCode;
    }

    public CustomException(RetCode retCode, String message) {
        super(message);
        this.retCode = retCode;
    }
}