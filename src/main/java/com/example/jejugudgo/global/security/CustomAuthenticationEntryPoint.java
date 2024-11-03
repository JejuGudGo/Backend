package com.example.jejugudgo.global.security;

import com.example.jejugudgo.global.exception.entity.ApiResponse;
import com.example.jejugudgo.global.exception.repository.ApiResponseRepository;
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
    private final ApiResponseRepository apiResponseRepository;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException instanceof UsernameNotFoundException) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            setResponse(response, "08");

        } else if (authException instanceof BadCredentialsException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            setResponse(response, "09");

        }
        else if (authException instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response, "98");
        }
    }

    private void setResponse(HttpServletResponse response, String errorCode) throws IOException {
        ApiResponse apiResponse = apiResponseRepository.findByRetCode(errorCode);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"retCode\" : \"" + apiResponse.getRetCode() + "\", " +
                 "\"retMessage\" : \"" + apiResponse.getRetMessage() + "\"}"
        );
    }
}
