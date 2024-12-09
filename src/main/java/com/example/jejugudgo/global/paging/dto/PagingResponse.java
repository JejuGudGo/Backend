package com.example.jejugudgo.global.paging.dto;

import java.util.List;

public record PagingResponse<T>(
        List<T> content,   // 실제 데이터 리스트
        int pageNumber,    // 현재 페이지 번호
        int totalPages,    // 전체 페이지 수
        long totalElements // 전체 요소 수
) {}

