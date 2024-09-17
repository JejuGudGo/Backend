//package com.gudgo.jeju.domain.bookmark.controller;
//
//import com.gudgo.jeju.domain.bookmark.dto.request.BookmarkCreateRequestDto;
//import com.gudgo.jeju.domain.bookmark.dto.response.BookMarkResponseDto;
//import com.gudgo.jeju.domain.bookmark.service.BookMarkService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequestMapping("/api/v1/bookmark")
//@RequiredArgsConstructor
//@Slf4j
//@RestController
//public class BookMarkController {
//    private final BookMarkService bookMarkService;
//
//
//    // 조회
//    @GetMapping(value = "")
//    public ResponseEntity<List<BookMarkResponseDto>> get(HttpServletRequest request) {
//        return ResponseEntity.ok(bookMarkService.get(request));
//    }
//
//    // 생성
//    @PostMapping(value = "")
//    public ResponseEntity<BookMarkResponseDto> create(HttpServletRequest request, @RequestBody BookmarkCreateRequestDto requestDto) {
//        bookMarkService.create(request, requestDto);
//        return ResponseEntity.ok().build();
//    }
//
//    // 삭제
//    @DeleteMapping(value = "/{bookmarkId}")
//    public ResponseEntity<?> delete(@PathVariable("bookmarkId") Long bookmarkId) {
//        bookMarkService.delete(bookmarkId);
//        return ResponseEntity.ok().build();
//    }
//}
