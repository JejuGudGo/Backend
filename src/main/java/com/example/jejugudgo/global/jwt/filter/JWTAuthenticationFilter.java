package com.example.jejugudgo.global.jwt.filter;

import com.example.jejugudgo.global.exception.entity.ApiResponse;
import com.example.jejugudgo.global.exception.repository.ApiResponseRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final ApiResponseRepository apiResponseRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (
                requestURI.startsWith("/api/v1/auth") ||
                        requestURI.startsWith("/docs") ||
                        requestURI.startsWith("/favicon.ico") ||
                        requestURI.startsWith("/ws") ||
                        requestURI.contains("index.html") ||
                        requestURI.contains("openapi.yml") ||
                        requestURI.startsWith("/api/v1/courses/olle")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = tokenUtil.getAccessTokenFromHeader(request);
        Long userId = tokenUtil.getUserIdFromToken(accessToken);

        try {
            tokenUtil.validateAccessToken(accessToken);
            tokenUtil.getAuthenticationUsingToken(accessToken, userId);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            setResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = apiResponseRepository.findByRetCode("98");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"retCode\" : \"" + apiResponse.getRetCode() + "\", " +
                 "\"retMessage\" : \"" + apiResponse.getRetMessage() + "\"}"
        );
    }
}
