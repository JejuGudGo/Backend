package com.example.jejugudgo.domain.user.athentication.term.controller;

import com.example.jejugudgo.domain.user.athentication.term.dto.TermResponse;
import com.example.jejugudgo.domain.user.athentication.term.service.TermService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth/terms")
@RequiredArgsConstructor
public class TermController {
    private final TermService termsService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping(value = "")
    public ResponseEntity<CommonApiResponse> getTerms() {
        List<TermResponse> responses = termsService.getTerms();
        return ResponseEntity.ok(apiResponseUtil.success(responses, "terms"));
    }
}
