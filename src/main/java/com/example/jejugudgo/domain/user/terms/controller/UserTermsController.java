package com.example.jejugudgo.domain.user.terms.controller;

import com.example.jejugudgo.domain.user.terms.dto.TermsAgreementRequest;
import com.example.jejugudgo.domain.user.terms.service.UserTermsService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/auth/terms")
@RequiredArgsConstructor
public class UserTermsController {
    private final UserTermsService termsService;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping(value = "/agree")
    public ResponseEntity<CommonApiResponse> agreeTerms(HttpServletRequest httpServletRequest, @RequestBody TermsAgreementRequest request) {
        termsService.agreeTerm(httpServletRequest, request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
