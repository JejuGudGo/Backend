package com.example.jejugudgo.domain.auth.basic.dto.request;

import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;


public record SignupRequest(
        TermsAgreementRequest terms,
        String email,
        String password,
        String name,
        String phoneNumber
) {
}
