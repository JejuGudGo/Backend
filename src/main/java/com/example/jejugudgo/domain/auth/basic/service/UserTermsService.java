package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.repository.UserTermsRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.CookieUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ognl.BooleanExpression;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserTermsService {
    private final UserRepository userRepository;
    private final UserTermsRepository userTermsRepository;
    private final TermsRepository termsRepository;
    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;

    public void saveUserAgreements(HttpServletRequest request, Map<Long, Boolean> termAgreements) {
        // 1. 쿠키에서 액세스 토큰 get
        Cookie accessTokenCookie = cookieUtil.getCookie(request, "accessToken");
        if (accessTokenCookie == null) {
            throw new IllegalArgumentException();
        }
        String accessToken = accessTokenCookie.getValue();

        // 2. 액세스 토큰 유효성 검증
        tokenUtil.validateAccessToken(accessToken);

        // 3. 토큰에서 userId 추출
        Long userId = tokenUtil.getUserIdFromToken(accessToken);

        // 4. 사용자 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        // 5. 약관 동의 상태 저장
        for (Map.Entry<Long, Boolean> entry : termAgreements.entrySet()) {
            Long termsId = entry.getKey();
            Boolean isAgreed = entry.getValue();

            Terms terms = termsRepository.findById(termsId)
                    .orElseThrow(EntityNotFoundException::new);

            UserTerms userTerms = UserTerms.builder()
                    .user(user)
                    .termsId(terms.getId())
                    .isAgreed(isAgreed)
                    .build();

            userTermsRepository.save(userTerms);
        }

    }
}
