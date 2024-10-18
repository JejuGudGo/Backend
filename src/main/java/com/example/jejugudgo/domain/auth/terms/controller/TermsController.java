package com.example.jejugudgo.domain.auth.terms.controller;

import com.example.jejugudgo.domain.auth.terms.dto.response.TermsResponse;
import com.example.jejugudgo.domain.auth.terms.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth/terms")
@RequiredArgsConstructor
public class TermsController {
    private final TermsService termsService;

    @GetMapping(value = "")
    public ResponseEntity<List<TermsResponse>> getTerms() {
        List<TermsResponse> responses = termsService.getTerms();
        return ResponseEntity.ok(responses);
    }
}
