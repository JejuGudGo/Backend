package com.gudgo.jeju.global.jwt;

import com.gudgo.jeju.global.jwt.token.TokenAuthenticator;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import com.gudgo.jeju.global.jwt.token.TokenValidator;
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
    private final TokenExtractor tokenExtractor;
    private final TokenValidator tokenValidator;
    private final TokenAuthenticator tokenAuthenticator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (
                requestURI.startsWith("/api/v1/auth") ||
                requestURI.startsWith("/api/v1/auth/token") ||
                requestURI.startsWith("/api/v1/oauth") ||
                requestURI.startsWith("/oauth") ||
                requestURI.startsWith("/docs") ||
                requestURI.startsWith("/favicon.ico") ||
                requestURI.startsWith("/ws") ||
                requestURI.contains("index.html") ||
                requestURI.contains("openapi.yml")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = tokenExtractor.getAccessTokenFromHeader(request);

        if (accessToken != null) {
            try {
                tokenValidator.validateAccessToken(accessToken);
                tokenAuthenticator.getAuthenticationUsingToken(accessToken);

            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                setResponse(response, "TOKEN_01");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setResponse(HttpServletResponse response, String errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                "{\"errorCode\" : \"" + errorCode + "\"}"
        );
    }
}