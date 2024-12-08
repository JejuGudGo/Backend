package com.example.jejugudgo.domain.course.search.elastic.doc;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "trail")
@Data
public class TrailDocument {
    @Field(type = FieldType.Long, index = false)
    Long id;

    @Field(type = FieldType.Text)
    List<String> tags;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    @Field(type = FieldType.Text)
    String title;

    @Field(type = FieldType.Text, index = false)
    private String address;

    @Field(type = FieldType.Text, index = false)
    private String content;

    @Field(type = FieldType.Text, index = false)
    private String tel;

    @Field(type = FieldType.Text, index = false)
    private String homepage;

    @Field(type = FieldType.Text, index = false)
    private String openTime;

    @Field(type = FieldType.Text, index = false)
    private String fee;

    @Field(type = FieldType.Text)
    private String time;

    @Field(type = FieldType.Text, index = false)
    private String thumbnailUrl;

    @Field(type = FieldType.Double, index = false)
    private Double latitude;

    @Field(type = FieldType.Double, index = false)
    private Double longitude;

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
}
