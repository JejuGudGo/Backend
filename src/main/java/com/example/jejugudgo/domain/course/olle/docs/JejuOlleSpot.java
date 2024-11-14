package com.example.jejugudgo.domain.course.olle.docs;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;

@Document(indexName = "jeju_olle_spot")
@Data
public class JejuOlleSpot {
    @Id
    private Long id;

    private String time;

    private double latitude;

    private double longitude;

    private Long spotOrder;

    private Long olleCourseId;
}
