package com.example.jejugudgo.global.jwt.token;

import com.example.jejugudgo.global.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenGenerator {
    private final RedisUtil redisUtil;
    static final long ACCESS_TOKEN_VALID_TIME = 30L * 24 * 60 * 60 * 1000; // 15 분간 유효.
    static final long REFRESH_TOKEN_VALID_TIME = 30L * 24 * 60 * 60 * 1000; // 60 분간 유효.

    @Autowired
    private Key key;

    public String generateToken (TokenType tokenType, String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        long extraTime = 0L;

        if (tokenType.equals(TokenType.ACCESS)) {
            extraTime = ACCESS_TOKEN_VALID_TIME;

        } else if (tokenType.equals(TokenType.REFRESH)) {
            extraTime = REFRESH_TOKEN_VALID_TIME;
        }

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + extraTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
