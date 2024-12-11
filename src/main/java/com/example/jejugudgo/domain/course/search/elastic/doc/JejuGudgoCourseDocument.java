package com.example.jejugudgo.domain.course.search.elastic.doc;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "jejugudgo_course")
public class JejuGudgoCourseDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Field(type = FieldType.Text, index = false)
    private String route;

    @Field(type = FieldType.Text, index = false)
    private String thumbnailUrl;

    @Field(type = FieldType.Text, index = false)
    private String content;

    @Field(type = FieldType.Text)
    private String time;

    @Field(type = FieldType.Long)
    private Long reviewCount;

    @Field(type = FieldType.Double)
    private Double starAvg;

    @Field(type = FieldType.Long)
    private Long likeCount;

    @Field(type = FieldType.Long)
    private Long clickCount;

    @Field(type = FieldType.Double)
    private Double upToDate;

    @Field(type = FieldType.Boolean)
    private boolean isDeleted = false;

    @Field(type = FieldType.Nested)
    private List<JejuGudgoSpotDocument> spots;

    @Field(type = FieldType.Nested)
    private List<JejuGudgoTagDocument> tags;
}
