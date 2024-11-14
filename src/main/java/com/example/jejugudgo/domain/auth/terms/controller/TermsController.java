package com.example.jejugudgo.domain.auth.terms.controller;

import com.example.jejugudgo.domain.auth.terms.dto.TermsResponse;
import com.example.jejugudgo.domain.auth.terms.service.TermsService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth/terms")
@RequiredArgsConstructor
public class TermsController {
    private final TermsService termsService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping(value = "")
    public ResponseEntity<CommonApiResponse> getTerms() {
        List<TermsResponse> responses = termsService.getTerms();
        return ResponseEntity.ok(apiResponseUtil.success(responses, "terms"));
    }
}
