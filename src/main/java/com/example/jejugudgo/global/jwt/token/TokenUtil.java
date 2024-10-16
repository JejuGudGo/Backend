package com.example.jejugudgo.global.jwt.token;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil {
    @Autowired
    private Key key;
    private final UserRepository userRepository;

    public void validateAccessToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public void getAuthenticationUsingToken(String accessToken, String userId) {
        if (accessToken.contains("Bearer")) {
            accessToken = accessToken.substring(7);
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(EntityNotFoundException::new);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user, accessToken, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

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
