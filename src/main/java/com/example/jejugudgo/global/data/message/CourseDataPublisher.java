package com.example.jejugudgo.global.data.message;

import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.global.message.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDataPublisher {
    private final KafkaPublisher kafkaPublisher;
    
    public void createOlleCourseMessagePublish() {
        kafkaPublisher.send("olle-course", "CREATE");
    }

    public void createTrailMessagePublish(Trail trail) {
        kafkaPublisher.send("trail-course", "CREATE");
    }
}
