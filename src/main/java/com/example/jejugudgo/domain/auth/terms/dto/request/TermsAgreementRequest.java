package com.example.jejugudgo.domain.auth.terms.dto.request;

public record TermsAgreementRequest(
        Long termsId,
        boolean isAgreed
) {
}
