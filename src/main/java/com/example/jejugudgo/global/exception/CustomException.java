package com.example.jejugudgo.global.exception;

import com.example.jejugudgo.global.exception.entity.RetCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final RetCode retCode;

    public CustomException(RetCode retCode) {
        super(retCode.getMessage());
        this.retCode = retCode;
    }
}