package com.gudgo.jeju.global.jwt.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@Slf4j
public class TokenExtractor {
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessTokenFromHeader = request.getHeader("Authorization")
                .substring(7);

        log.info("===========================================================================");
        log.info("AccessToken from header: " + accessTokenFromHeader);
        log.info("===========================================================================");

        if (!accessTokenFromHeader.isEmpty()) {
            return accessTokenFromHeader;
        }

        return null;
    }

    public String getAccessTokenFromRequest (ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        String accessToken = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams().getFirst("token");

        log.info("===========================================================================");
        log.info("AccessToken from request: " + accessToken);
        log.info("===========================================================================");

        if (!accessToken.isEmpty()) {
            return accessToken;
        }

        return null;
    }
}
