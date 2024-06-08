package com.gudgo.jeju.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<?> handleAuthenticationServiceException() {
        ErrorResponse errorResponse = new ErrorResponse("AUTH_04");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
