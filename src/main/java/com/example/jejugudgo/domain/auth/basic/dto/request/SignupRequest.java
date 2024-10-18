package com.example.jejugudgo.domain.auth.basic.dto.request;

import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;

import java.util.List;

public record SignupRequest(
        List<TermsAgreementRequest> terms,
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
