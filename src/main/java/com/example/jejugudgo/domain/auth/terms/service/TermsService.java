package com.example.jejugudgo.domain.auth.terms.service;

import com.example.jejugudgo.domain.auth.terms.dto.TermsResponse;
import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsService {
    private final TermsRepository termsRepository;

    public List<TermsResponse> getTerms() {
        List<Terms> terms = termsRepository.findAll();
        List<TermsResponse> responses = new ArrayList<>();

        for (Terms term : terms) {
            TermsResponse response = new TermsResponse(term.getId(), term.getTitle(), term.getContent());
            responses.add(response);
        }

        return responses;
    }
}
