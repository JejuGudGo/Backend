package com.gudgo.jeju.domain.post.column.dto.response;


import java.util.List;

public record ColumnPostResponse(
        Long postId,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long numberTag,
        String title,
        String content,
        List<PostImageResponse> images
) {
}
