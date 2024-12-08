package com.example.jejugudgo.domain.course.search.elastic.doc;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class JejuGudgoSpotDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Double, index = false)
    private Double latitude;

    @Field(type = FieldType.Double, index = false)
    private Double longitude;

    @Field(type = FieldType.Long, index = false)
    private Long spotOrder;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;
}
