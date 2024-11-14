package com.example.jejugudgo.domain.user.terms.dto;

public record TermsAgreementRequest(
        boolean isAgreed,
        String agreedAt
) {
}
