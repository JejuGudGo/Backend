package com.example.jejugudgo.global.exception.repository;

import com.example.jejugudgo.global.exception.entity.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiResponseRepository extends JpaRepository<ApiResponse, Long> {
    ApiResponse findByRetCode(String retCode);
}
