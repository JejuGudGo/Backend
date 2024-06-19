package com.gudgo.jeju.domain.post.service;

import com.gudgo.jeju.domain.post.dto.request.ColumnPostCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.ColumnPostUpdateRequest;
import com.gudgo.jeju.domain.post.dto.response.ColumnPostResponse;
import com.gudgo.jeju.domain.post.entity.PostType;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.repository.PostsRepository;
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

    public ColumnPostResponse get(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        ColumnPostResponse columnPostResponse = new ColumnPostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                post.getUser().getProfile().getProfileImageUrl(),
                post.getUser().getNumberTag(),
                post.getTitle(),
                post.getContent()
        );

        return columnPostResponse;
    }

    @Transactional
    public void create(ColumnPostCreateRequest request, MultipartFile[] images) throws Exception {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EntityNotFoundException::new);

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
    }

    @Transactional
    public void update(Long postId, ColumnPostUpdateRequest request) {
        Posts posts  = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateStringValue(request.title())) {
            posts.withTitle(request.title());
        }

        if (validationUtil.validateStringValue(request.content())) {
            posts.withContent(request.content());
        }

        postsRepository.save(posts);
    }

    @Transactional
    public void delete(Long postId) {
        Posts posts  = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        if (validationUtil.validateLongValue(postId)) {
            posts.withIsDeleted(true);
        }

        postsRepository.save(posts);
    }
}
