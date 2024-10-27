package com.example.jejugudgo.domain.bookmark.controller;

import com.example.jejugudgo.domain.bookmark.dto.request.BookMarkRequest;
import com.example.jejugudgo.domain.bookmark.dto.response.BookMarkResponse;
import com.example.jejugudgo.domain.bookmark.service.BookMarkService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bookmark")
@RequiredArgsConstructor
public class BookMarkController {
    private final BookMarkService bookMarkService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getBookMarks(@RequestParam("type") String query, HttpServletRequest request) {
        List<BookMarkResponse> responses = bookMarkService.getBookMarks(query, request);
        return ResponseEntity.ok(apiResponseUtil.success(responses));
    }

    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest servletRequest,@RequestBody BookMarkRequest bookMarkRequest) {
        bookMarkService.create(servletRequest, bookMarkRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<CommonApiResponse> delete(@PathVariable("bookmarkId") Long bookMarkId, HttpServletRequest request) {
        bookMarkService.delete(bookMarkId, request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
