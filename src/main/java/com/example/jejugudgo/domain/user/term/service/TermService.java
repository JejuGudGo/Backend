package com.example.jejugudgo.domain.user.term.service;

import com.example.jejugudgo.domain.user.term.dto.TermResponse;
import com.example.jejugudgo.global.data.term.entity.Term;
import com.example.jejugudgo.global.data.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TermService {
    private final TermRepository termsRepository;

    public List<TermResponse> getTerms() {
        List<Term> terms = termsRepository.findAll();
        List<TermResponse> responses = new ArrayList<>();

        for (Term term : terms) {
            TermResponse response = new TermResponse(term.getId(), term.getTitle(), term.getContent());
            responses.add(response);
        }

        return responses;
    }
}
