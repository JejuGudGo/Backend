package com.example.jejugudgo.domain.trail.message;

import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrailPublisher {
    private final KafkaPublisher kafkaPublisher;
    private final BookmarkRepository bookmarkRepository;

    public void createTrailMessagePublish(Trail trail) {
        List<Long> bookmarkUsers = bookmarkRepository.findDistinctUserByBookMarkTypeAndTargetId(BookmarkType.TRAIL, trail.getId());
        TrailDocument trailDocument = TrailDocument.of(trail, bookmarkUsers);
        kafkaPublisher.sendData("trail_topic", trailDocument, "CREATE");
    }

    public void updateTrailMessagePublish(Trail trail, double newStarAvg) {
        StarAvgUpdateRequest request = new StarAvgUpdateRequest(trail.getId(), newStarAvg);
        kafkaPublisher.sendData("trail_topic", request, "UPDATE_STAR_AVG");
    }
}
