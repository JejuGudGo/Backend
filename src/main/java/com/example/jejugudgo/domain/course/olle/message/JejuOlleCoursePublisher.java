package com.example.jejugudgo.domain.course.olle.message;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleSpotRepository;
import com.example.jejugudgo.domain.course.olle.service.JejuOlleCourseDocumentService;
import com.example.jejugudgo.domain.review.dto.request.StarAvgUpdateRequest;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JejuOlleCoursePublisher {
    private final KafkaPublisher kafkaPublisher;
    private final JejuOlleSpotRepository jejuOlleSpotRepository;
    private final JejuOlleCourseDocumentService jejuOlleCourseDocumentService;

    public void jejuOlleCourseMessagePublish(JejuOlleCourse jejuOlleCourse) {
        List<JejuOlleSpot> jejuOlleSpots = jejuOlleSpotRepository.findByJejuOlleCourse(jejuOlleCourse);
        JejuOlleCourseDocument jejuOlleCourseDocument = jejuOlleCourseDocumentService.documentsJejuOlleCourse(jejuOlleCourse, jejuOlleSpots);
        kafkaPublisher.sendData("olle_topic", jejuOlleCourseDocument, "CREATE");
    }

    public void updateJejuOlleStarAvgMessagePublish(JejuOlleCourse jejuOlleCourse, double newStarAvg) {
        StarAvgUpdateRequest request = new StarAvgUpdateRequest(jejuOlleCourse.getId(), newStarAvg);
        kafkaPublisher.sendData("olle_topic", request, "UPDATE_STAR_AVG");
    }
}
