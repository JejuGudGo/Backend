package com.example.jejugudgo.domain.course.search.elastic.doc;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class JejuGudgoTagDocument {
    @Field(type = FieldType.Text)
    private String title;
}
