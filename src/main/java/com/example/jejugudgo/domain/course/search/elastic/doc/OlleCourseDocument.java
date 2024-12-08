package com.example.jejugudgo.domain.course.search.elastic.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "olle_course")
@Data
public class OlleCourseDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Field(type = FieldType.Text, index = false)
    private String route;

    @Field(type = FieldType.Text, index = false)
    private String distance;

    @Field(type = FieldType.Text)
    private String time;

    @Field(type = FieldType.Text, index = false)
    private String summary;

    @Field(type = FieldType.Text, index = false)
    private String content;

    @Field(type = FieldType.Double, index = false)
    private Double latitude;

    @Field(type = FieldType.Double, index = false)
    private Double longitude;

    @Field(type = FieldType.Text, index = false)
    private String address;

    @Field(type = FieldType.Text, index = false)
    private String openTime;

    @Field(type = FieldType.Text, index = false)
    private String tel;

    @Field(type = FieldType.Text, index = false)
    private String thumbnailUrl;

    @Field(type = FieldType.Long)
    private Long reviewCount;

    @Field(type = FieldType.Double)
    private Double starAvg;

    @Field(type = FieldType.Long)
    private Long likeCount;

    @Field(type = FieldType.Double)
    private Double upToDate;

    @Field(type = FieldType.Nested)
    private List<OlleSpotDocument> spots;

    @Field(type = FieldType.Nested)
    private List<OlleCourseTagDocument> tags;
}
