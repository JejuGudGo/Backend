package com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request;


import java.util.List;

public record UpdateBookmarkElasticDataRequest(
        Long courseId,
        List<Long> bookmarkUsers
) {
}
