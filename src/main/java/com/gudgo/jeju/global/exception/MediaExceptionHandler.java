package com.gudgo.jeju.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class MediaExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException() {
        ErrorResponse errorResponse = new ErrorResponse("IMAGE_01");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
