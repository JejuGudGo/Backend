package com.gudgo.jeju.domain.post.participant.validation;

import com.gudgo.jeju.domain.post.participant.query.ParticipantQueryService;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.repository.PostsRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantValidator {
    private final ParticipantQueryService participantQueryService;
    private final PostsRepository postsRepository;

    public void validateParticipantNumber(Long postId, Long courseId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(EntityExistsException::new);

        Long currentNumber = participantQueryService.countCourseParticipant(courseId);

        if (posts.getParticipantNum() == currentNumber) {
            throw new IllegalStateException("The number of companions is already full for courseId=" + courseId);
        }
    }
}
