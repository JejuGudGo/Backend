package com.example.jejugudgo.domain.course.jejugudgo.docs;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class JejuGudgoCourseTagDocument {
    @Id
    private Long tagId;

    private String tag;


    public static JejuGudgoCourseTagDocument of(JejuGudgoCourseTag jejuGudgoCourseTag) {
        JejuGudgoCourseTagDocument document = new JejuGudgoCourseTagDocument();
        document.setTagId(jejuGudgoCourseTag.getId());
        document.setTag(jejuGudgoCourseTag.getCourseTag().getTag());
        return document;
    }
}
