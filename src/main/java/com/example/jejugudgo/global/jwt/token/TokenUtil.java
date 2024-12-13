package com.example.jejugudgo.global.jwt.token;

import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.security.CustomAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;


@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil {
    @Autowired
    private Key key;
    private final UserRepository userRepository;

    public void validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public void getAuthenticationUsingToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                user.getProvider()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessTokenFromHeader = request.getHeader("Authorization")
                .substring(7);

        log.info("===============================================================================");
        log.info("AccessToken from header: " + accessTokenFromHeader);
        log.info("===============================================================================");


        if (!accessTokenFromHeader.isEmpty()) {
            return accessTokenFromHeader;
        }

        return null;
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

    public Long getUserIdFromHeader(HttpServletRequest request) {
        String accessToken = getAccessTokenFromHeader(request);
        Long userId = getUserIdFromToken(accessToken);
        return userId;
    }
}
