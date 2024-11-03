package com.example.jejugudgo.domain.auth.terms.dto.request;

public record TermsAgreementRequest(
        boolean isAgreed,
        String agreedAt
) {
}
