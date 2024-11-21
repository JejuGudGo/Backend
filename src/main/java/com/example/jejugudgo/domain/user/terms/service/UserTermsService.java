package com.example.jejugudgo.domain.user.terms.service;

import com.example.jejugudgo.domain.user.terms.dto.TermsAgreementRequest;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.terms.entity.UserTerms;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.terms.repository.UserTermsRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserTermsService {
    private final UserRepository userRepository;
    private final UserTermsRepository userTermsRepository;
    private final TokenUtil tokenUtil;

    public void agreeTerm(HttpServletRequest httpServletRequest, TermsAgreementRequest request) {
        User user = userRepository.findById(tokenUtil.getUserIdFromHeader(httpServletRequest))
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        UserTerms userTerms = userTermsRepository.findByUser(user)
                .orElse(null);

        if (userTerms == null) {
            UserTerms newUserTerms = UserTerms.builder()
                    .agreedAt(request.agreedAt())
                    .isAgreed(request.isAgreed())
                    .user(user)
                    .build();

            userTermsRepository.save(newUserTerms);

        } else {
            userTerms = userTerms.updateUserTerms(request.isAgreed(), request.agreedAt());
            userTermsRepository.save(userTerms);
        }
    }
}

