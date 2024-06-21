package com.gudgo.jeju.domain.post.dto.response;


public record ColumnPostResponse(
        Long postId,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long numberTag,
        String title,
        String content
) {
}