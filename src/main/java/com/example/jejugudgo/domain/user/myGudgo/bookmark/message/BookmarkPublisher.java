package com.example.jejugudgo.domain.user.myGudgo.bookmark.message;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.request.UpdateBookmarkElasticDataRequest;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookmarkPublisher {
    private final KafkaPublisher kafkaPublisher;
    private final BookmarkRepository bookmarkRepository;

    public void updateJejuGudgoBookmarkUsers(JejuGudgoCourse course) {
        List<Long> bookmarkUsers = bookmarkRepository.findDistinctUserByBookMarkTypeAndTargetId(BookmarkType.JEJU_GUDGO, course.getId());
        UpdateBookmarkElasticDataRequest request = new UpdateBookmarkElasticDataRequest(
                course.getId(),
                bookmarkUsers
        );
        kafkaPublisher.sendData("jejugudgo_topic", request, "UPDATE_BOOKMARK_USERS");
    }

    public void updateJejuOlleBookmarkUsers(JejuOlleCourse course) {
        List<Long> bookmarkUsers = bookmarkRepository.findDistinctUserByBookMarkTypeAndTargetId(BookmarkType.OLLE, course.getId());
        UpdateBookmarkElasticDataRequest request = new UpdateBookmarkElasticDataRequest(
                course.getId(),
                bookmarkUsers
        );
        kafkaPublisher.sendData("olle_topic", request, "UPDATE_BOOKMARK_USERS");
    }

    public void updateTrailBookmarkUsers(Trail trail) {
        List<Long> bookmarkUsers = bookmarkRepository.findDistinctUserByBookMarkTypeAndTargetId(BookmarkType.TRAIL, trail.getId());
        UpdateBookmarkElasticDataRequest request = new UpdateBookmarkElasticDataRequest(
                trail.getId(),
                bookmarkUsers
        );
        kafkaPublisher.sendData("trail_topic", request, "UPDATE_BOOKMARK_USERS");
    }
}
