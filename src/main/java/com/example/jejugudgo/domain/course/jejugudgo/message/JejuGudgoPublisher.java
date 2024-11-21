package com.example.jejugudgo.domain.course.jejugudgo.message;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.course.jejugudgo.service.JejuGudgoCourseDocumentService;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JejuGudgoPublisher {
    private final KafkaPublisher kafkaPublisher;
    private final JejuGudgoCourseDocumentService jejuGudgoCourseDocumentService;

    public void createJejuGudgoCourseMessagePublish(JejuGudgoCourse jejuGudgoCourse, List<JejuGudgoCourseSpot> spots, List<JejuGudgoCourseTag> tags) {
        JejuGudgoCourseDocument jejuGudgoCourseDocument = jejuGudgoCourseDocumentService.documentsJejuCourse(jejuGudgoCourse, spots, tags);
        kafkaPublisher.sendData("jejugudgo_topic", jejuGudgoCourseDocument, "CREATE");
    }

    public void updateJejuGudgoStarAvgMessagePublish(JejuGudgoCourse jejuGudgoCourse, double newStarAvg) {
        StarAvgUpdateRequest request = new StarAvgUpdateRequest(jejuGudgoCourse.getId(), newStarAvg);
        kafkaPublisher.sendData("jejugudgo_topic", request, "UPDATE_STAR_AVG");
    }

    // TODO: elastic 에서 특정 course 수정
    // TODO: elastic 에서 특정 course 삭제
}
