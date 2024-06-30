package com.gudgo.jeju.global.webSocket;

import com.gudgo.jeju.global.jwt.token.TokenAuthenticator;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import com.gudgo.jeju.global.jwt.token.TokenValidator;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class CustomHandshakeHandler implements HandshakeHandler {
    private final TokenExtractor tokenExtractor;
    private final TokenAuthenticator tokenAuthenticator;
    private final TokenValidator tokenValidator;

    @Override
    public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {
        try {
            String accessToken = tokenExtractor.getAccessTokenFromRequest(request);
            tokenValidator.validateAccessToken(accessToken);
            tokenAuthenticator.getAuthenticationUsingToken(accessToken);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            log.info("===========================================================================");
            log.info("Authentication: " + authentication.toString());
            log.info("===========================================================================");

        } catch (ExpiredJwtException | InsufficientAuthenticationException exception) {
            try {
                setResponse(response, HttpStatus.UNAUTHORIZED, "TOKEN_01");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return false;
        }

        return true;
    }

    private void setResponse(ServerHttpResponse response, HttpStatus status, String errorCode) throws IOException {
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        String errorMessage = "{\"errorCode\": \"" + errorCode + "\"}";
        response.getBody().write(errorMessage.getBytes(StandardCharsets.UTF_8));
        response.getBody().flush();
    }
}
