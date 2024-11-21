package com.example.jejugudgo.domain.user.myGudgo.bookmark.controller;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.BookmarkRequest;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.response.BookmarkResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.service.BookmarkService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookMarkService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getBookMarks(@RequestParam("type") String query, HttpServletRequest request) {
        List<BookmarkResponse> responses = bookMarkService.getBookMarks(query, request);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "bookmarks"));
    }

    @PostMapping("")
    public ResponseEntity<CommonApiResponse> create(HttpServletRequest servletRequest,@RequestBody BookmarkRequest bookMarkRequest) {
        bookMarkService.create(servletRequest, bookMarkRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<CommonApiResponse> delete(@PathVariable("bookmarkId") Long bookMarkId, HttpServletRequest request) {
        bookMarkService.delete(bookMarkId, request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
