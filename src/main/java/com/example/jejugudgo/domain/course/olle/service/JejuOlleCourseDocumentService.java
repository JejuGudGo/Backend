package com.example.jejugudgo.domain.course.olle.service;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import com.example.jejugudgo.domain.course.olle.docs.JejuOlleSpotDocument;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuOlleCourseDocumentService {

    public JejuOlleCourseDocument documentsJejuOlleCourse(JejuOlleCourse jejuOlleCourse, List<JejuOlleSpot> jejuOlleSpots) {
        List<JejuOlleSpotDocument> jejuOlleSpotDocuments = documentsJejuOlleSpot(jejuOlleSpots);
        JejuOlleCourseDocument jejuOlleCourseDocument = JejuOlleCourseDocument.of(jejuOlleCourse, jejuOlleSpotDocuments);

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
