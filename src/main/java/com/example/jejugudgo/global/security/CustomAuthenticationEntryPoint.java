package com.example.jejugudgo.global.security;

import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        CustomException customException = (CustomException) request.getAttribute("customException");
        if (customException != null) {
            setResponse(response, customException);
            return;
        }

        if (authException instanceof UsernameNotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            setResponse(response, RetCode.RET_CODE08);

        } else if (authException instanceof BadCredentialsException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            setResponse(response, RetCode.RET_CODE09);

        }
        else if (authException instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response, RetCode.RET_CODE98);
        }
    }

    private void setResponse(HttpServletResponse response, RetCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"retCode\" : \"" + errorCode.getRetCode() + "\", " +
                 "\"retMessage\" : \"" + errorCode.getMessage() + "\"}"
        );
    }

    private void setResponse(HttpServletResponse response, CustomException customException) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"retCode\" : \"" + customException.getRetCode().getRetCode() + "\", " +
                        "\"retMessage\" : \"" + customException.getMessage() + "\"}"
        );
    }
}
