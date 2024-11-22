package com.example.jejugudgo.domain.course.olle.service;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.course.olle.docs.JejuOlleSpotDocument;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourseTag;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuOlleCourseDocumentService {
    private final BookmarkRepository bookmarkRepository;

    public JejuOlleCourseDocument documentsJejuOlleCourse(JejuOlleCourse jejuOlleCourse, List<JejuOlleSpot> jejuOlleSpots, List<String> olleTags) {
        List<JejuOlleSpotDocument> jejuOlleSpotDocuments = documentsJejuOlleSpot(jejuOlleSpots);
        JejuOlleCourseDocument jejuOlleCourseDocument = JejuOlleCourseDocument.of(jejuOlleCourse, jejuOlleSpotDocuments, olleTags);

        return jejuOlleCourseDocument;
    }

    private List<JejuOlleSpotDocument> documentsJejuOlleSpot(List<JejuOlleSpot> olleSpots) {
        List<JejuOlleSpotDocument> jejuOlleSpotDocuments = new ArrayList<>();

        for (JejuOlleSpot jejuOlleSpot : olleSpots) {
            JejuOlleSpotDocument jejuOlleSpotDocument = JejuOlleSpotDocument.of(jejuOlleSpot);
            jejuOlleSpotDocuments.add(jejuOlleSpotDocument);
        }
        return jejuOlleSpotDocuments;
    }
}
