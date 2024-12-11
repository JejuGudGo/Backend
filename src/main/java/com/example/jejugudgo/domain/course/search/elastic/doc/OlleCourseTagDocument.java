package com.example.jejugudgo.domain.course.search.elastic.doc;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class OlleCourseTagDocument {
    @Field(type = FieldType.Text)
    String title;
}
