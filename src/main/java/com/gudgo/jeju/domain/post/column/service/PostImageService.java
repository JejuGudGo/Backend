package com.gudgo.jeju.domain.post.column.service;

import com.gudgo.jeju.domain.post.column.dto.response.PostImageResponse;
import com.gudgo.jeju.domain.post.common.entity.PostImage;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.column.query.PostImageQueryService;
import com.gudgo.jeju.domain.post.column.repository.PostImageRepository;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageDeleteService;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final PostsRepository postsRepository;
    private final ImageUpdateService imageUpdateService;
    private final ImageDeleteService imageDeleteService;
    private final PostImageQueryService postImageQueryService;

    public List<PostImageResponse> getImages(Long postId) {
        List<PostImageResponse> postImageRepons = postImageQueryService.getPostImages(postId);
        return postImageRepons;
    }

    @Transactional
    public void uploadImages (Long postId, Long userId, MultipartFile[] images) throws Exception {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        for(MultipartFile image : images) {
            Path path = imageUpdateService.saveImage(userId, image, Category.COLUMN);
            PostImage postImage = PostImage.builder()
                    .imageUrl(path.toString())
                    .posts(posts)
                    .isDeleted(false)
                    .build();

            postImageRepository.save(postImage);
        }
    }

    @Transactional
    public void deleteImage(Long postImageId) throws IOException {
        PostImage postImage = postImageRepository.findById(postImageId)
                .orElseThrow(EntityNotFoundException::new);

        postImage = postImage.withIsDeleted(true);
        postImageRepository.save(postImage);

        imageDeleteService.deleteImageWithUrl(postImage.getImageUrl());
    }
}
