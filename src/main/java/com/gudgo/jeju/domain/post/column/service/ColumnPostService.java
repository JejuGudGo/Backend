package com.gudgo.jeju.domain.post.column.service;

import com.gudgo.jeju.domain.post.column.dto.request.ColumnPostCreateRequest;
import com.gudgo.jeju.domain.post.column.dto.request.ColumnPostUpdateRequest;
import com.gudgo.jeju.domain.post.column.dto.response.ColumnPostResponse;
import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.column.query.PostImageQueryService;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ColumnPostService {
    private final PostsRepository postsRepository;
    private final PostImageService postImageService;
    private final UserRepository userRepository;

    private final ValidationUtil validationUtil;
    private final PostImageQueryService postImageQueryService;

    public ColumnPostResponse get(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        ColumnPostResponse response = new ColumnPostResponse(
                postId,
                posts.getUser().getId(),
                posts.getUser().getNickname(),
                posts.getUser().getProfile().getProfileImageUrl(),
                posts.getUser().getNumberTag(),
                posts.getTitle(),
                posts.getContent(),
                postImageQueryService.getPostImages(posts.getId())
        );

        return response;
    }

    @Transactional
    public ColumnPostResponse create(ColumnPostCreateRequest request, MultipartFile[] images) throws Exception {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        if (user.getRole().equals(Role.AUTHOR)) {
            Posts posts = Posts.builder()
                    .user(user)
                    .postType(PostType.COLUMN)
                    .title(request.title())
                    .content(request.content())
                    .createdAt(LocalDate.now())
                    .isDeleted(false)
                    .build();

            postsRepository.save(posts);

            postImageService.uploadImages(posts.getId(), user.getId(), images);

            ColumnPostResponse response = new ColumnPostResponse(
                    posts.getId(),
                    posts.getUser().getId(),
                    posts.getUser().getNickname(),
                    posts.getUser().getProfile().getProfileImageUrl(),
                    posts.getUser().getNumberTag(),
                    posts.getTitle(),
                    posts.getContent(),
                    postImageQueryService.getPostImages(posts.getId())
            );

            return response;

        } else {
            throw new IllegalAccessException("POST_01");
        }
    }

    @Transactional
    public ColumnPostResponse update(Long postId, ColumnPostUpdateRequest request) {
        Posts posts  = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (posts.getUser().getRole().equals(Role.AUTHOR)) {
            if (validationUtil.validateStringValue(request.title())) {
                posts = posts.withTitle(request.title());
            }

            if (validationUtil.validateStringValue(request.content())) {
                posts = posts.withContent(request.content());
            }
            postsRepository.save(posts);
        }

        ColumnPostResponse response = new ColumnPostResponse(
                postId,
                posts.getUser().getId(),
                posts.getUser().getNickname(),
                posts.getUser().getProfile().getProfileImageUrl(),
                posts.getUser().getNumberTag(),
                posts.getTitle(),
                posts.getContent(),
                postImageQueryService.getPostImages(posts.getId())
        );

        return response;
    }

    @Transactional
    public void delete(Long postId) {
        Posts posts  = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (posts.getUser().getRole().equals(Role.AUTHOR)) {
            if (validationUtil.validateLongValue(postId)) {
                posts = posts.withIsDeleted(true);
            }

            postsRepository.save(posts);
        }
    }
}
