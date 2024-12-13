package com.example.jejugudgo.domain.home.trail;

import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/courses/trail")
@RequiredArgsConstructor
public class TrailController {
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping
    public ResponseEntity<?> getTrails() {
        return ResponseEntity.ok().build();
    }
}
