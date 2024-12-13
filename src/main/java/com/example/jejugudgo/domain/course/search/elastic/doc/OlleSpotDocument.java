package com.example.jejugudgo.domain.course.search.elastic.doc;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class OlleSpotDocument {
    @Field(type = FieldType.Long, index = false)
    Long id;

    @Field(type = FieldType.Text)
    String title;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Field(type = FieldType.Long, index = false)
    Double latitude;

    @Field(type = FieldType.Double, index = false)
    Double longitude;

    @Field(type = FieldType.Double, index = false)
    Long spotOrder;
}
