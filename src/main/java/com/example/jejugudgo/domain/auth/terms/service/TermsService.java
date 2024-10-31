package com.example.jejugudgo.domain.auth.terms.service;

import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;
import com.example.jejugudgo.domain.auth.terms.dto.response.TermsResponse;
import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.repository.UserTermsRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsService {
    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final UserTermsRepository userTermsRepository;
    private final TokenUtil tokenUtil;

    public List<TermsResponse> getTerms() {
        List<Terms> terms = termsRepository.findAll();
        List<TermsResponse> responses = new ArrayList<>();

        for (Terms term : terms) {
            TermsResponse response = new TermsResponse(term.getId(), term.getTitle(), term.getContent());
            responses.add(response);
        }

        return responses;
    }

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
