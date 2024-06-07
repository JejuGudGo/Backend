package com.gudgo.jeju.global.jwt.token;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@Slf4j
public class SubjectExtractor {

    @Autowired
    private Key key;

    public Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userIdFromToken = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Long userId = Long.parseLong(userIdFromToken);

        log.info("===========================================================================");
        log.info("Extracted UserID from Token: " + userId);
        log.info("===========================================================================");

        return userId;
    }
}
