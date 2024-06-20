package com.gudgo.jeju.domain.post.service;

import com.gudgo.jeju.domain.post.dto.request.CommentCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CommentUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.CommentResponse;
import com.gudgo.jeju.domain.post.dto.response.NestedCommentResponse;
import com.gudgo.jeju.domain.post.entity.Comment;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.CommentRepository;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final PostsRepository postsRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Comment comment = Comment.builder()
                .posts(posts)
                .userId(request.userId())
                .content(request.content())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);

        CommentResponse commentResponse = new CommentResponse(
                comment.getId(),
                user.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                user.getNumberTag(),
                comment.getContent()
        );

        return commentResponse;
    }

    @Transactional
    public NestedCommentResponse createNestedComment(Long postId, Long commentId, CommentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Comment comment = Comment.builder()
                .posts(posts)
                .parentCommentId(commentId)
                .userId(request.userId())
                .content(request.content())
                .isDeleted(false)
                .build();

        commentRepository.save(comment);

        NestedCommentResponse commentResponse = new NestedCommentResponse(
                comment.getParentCommentId(),
                comment.getId(),
                user.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                user.getNumberTag(),
                comment.getContent()
        );

        return commentResponse;
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(comment.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(comment.getContent())) {
            comment = comment.withContent(request.content());
        }

        commentRepository.save(comment);

        CommentResponse commentResponse = new CommentResponse(
                commentId,
                user.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                user.getNumberTag(),
                comment.getContent()
        );

        return commentResponse;
    }

    @Transactional
    public NestedCommentResponse updateNestedComment(Long nestedId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(nestedId)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(comment.getContent())) {
            comment = comment.withContent(request.content());
        }

        commentRepository.save(comment);

        NestedCommentResponse commentResponse = new NestedCommentResponse(
                comment.getParentCommentId(),
                comment.getId(),
                user.getId(),
                user.getNickname(),
                user.getProfile().getProfileImageUrl(),
                user.getNumberTag(),
                comment.getContent()
        );

        return commentResponse;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        comment = comment.withIsDeleted(true);

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteNestedComment(Long nestedId) {
        Comment comment = commentRepository.findById(nestedId)
                .orElseThrow(EntityNotFoundException::new);

        comment = comment.withIsDeleted(true);

        commentRepository.save(comment);
    }
}
