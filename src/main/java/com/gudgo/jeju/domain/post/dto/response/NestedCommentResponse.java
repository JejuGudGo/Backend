package com.gudgo.jeju.domain.post.dto.response;

public record NestedCommentResponse(
        Long commentId,
        Long nestedId,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long numberTag,
        String content
) {
}
