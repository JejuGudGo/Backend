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
    static final long ACCESS_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000; // 하루간 유효.

    @Autowired
    private Key key;

    public String generateToken (String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + token;
    }
}
