package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseSpotDocument;
import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseTagDocument;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseDocumentRepository;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseDocumentService {
    private final JejuGudgoCourseDocumentRepository jejuGudgoCourseDocumentRepository;
    private final RedisUtil redisUtil;
    private final BookmarkRepository bookmarkRepository;

    public JejuGudgoCourseDocument documentsJejuCourse(JejuGudgoCourse jejuGudgoCourse, List<JejuGudgoCourseSpot> spots, List<JejuGudgoCourseTag> tags) {
        List<JejuGudgoCourseSpotDocument> spotDocuments = documentsJejuCourses(spots);
        List<JejuGudgoCourseTagDocument> tagDocuments = documentsJejuCourseTags(tags);
        List<Long> bookmarkUsers = bookmarkRepository.findDistinctUserByBookMarkTypeAndTargetId(BookmarkType.JEJU_GUDGO, jejuGudgoCourse.getId());

        JejuGudgoCourseDocument jejuGudgoCourseDocument = JejuGudgoCourseDocument.of(jejuGudgoCourse, spotDocuments, tagDocuments, bookmarkUsers);
        return jejuGudgoCourseDocument;
    }

    private List<JejuGudgoCourseSpotDocument> documentsJejuCourses(List<JejuGudgoCourseSpot> spots) {
        List<JejuGudgoCourseSpotDocument> courseSpotDocuments = new ArrayList<>();

        for (JejuGudgoCourseSpot spot : spots) {
            JejuGudgoCourseSpotDocument document = JejuGudgoCourseSpotDocument.of(spot);
            courseSpotDocuments.add(document);
        }

        return courseSpotDocuments;
    }

    private List<JejuGudgoCourseTagDocument> documentsJejuCourseTags(List<JejuGudgoCourseTag> tags) {
        List<JejuGudgoCourseTagDocument> courseTagDocuments = new ArrayList<>();

        for (JejuGudgoCourseTag tag : tags) {
            JejuGudgoCourseTagDocument document = JejuGudgoCourseTagDocument.of(tag);
            courseTagDocuments.add(document);
        }

        return courseTagDocuments;
    }

    // TODO: 코스 수정시 도큐먼트화 하는 코드 -> 퍼블리셔에서 사용
}
