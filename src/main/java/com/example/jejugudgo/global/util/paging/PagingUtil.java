package com.example.jejugudgo.global.util.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagingUtil {
    private static final int DEFAULT_PAGE = 0; // 기본 페이지 번호
    private static final int DEFAULT_SIZE = 10; // 기본 페이지 크기
    private static final int MAX_SIZE = 100; // 최대 페이지 크기

    /**
     * @param page 요청된 페이지 번호 (nullable)
     * @param size 요청된 페이지 크기 (nullable)
     */
    public static Pageable createPageable(Integer page, Integer size) {
        int validatedPage = (page == null || page < 0) ? DEFAULT_PAGE : page;
        int validatedSize = (size == null || size <= 0 || size > MAX_SIZE) ? DEFAULT_SIZE : size;
        return PageRequest.of(validatedPage, validatedSize);
    }
}