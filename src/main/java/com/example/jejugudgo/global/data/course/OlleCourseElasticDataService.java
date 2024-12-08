package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import com.example.jejugudgo.domain.course.common.entity.OlleSpot;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseTagRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleSpotRepository;
import com.example.jejugudgo.domain.course.search.elastic.doc.OlleCourseDocument;
import com.example.jejugudgo.domain.course.search.elastic.doc.OlleCourseTagDocument;
import com.example.jejugudgo.domain.course.search.elastic.doc.OlleSpotDocument;
import com.example.jejugudgo.domain.course.search.elastic.repository.OlleCourseDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OlleCourseElasticDataService {
    private final OlleCourseRepository olleCourseRepository;
    private final OlleCourseTagRepository olleCourseTagRepository;
    private final OlleCourseDocumentRepository olleCourseDocumentRepository;
    private final OlleSpotRepository olleSpotRepository;


    public void createOlleCourseToElastic() {
        List<OlleCourse> olleCourses = olleCourseRepository.findAll();
        for (OlleCourse olleCourse : olleCourses) {
            List<OlleSpot> spots = olleSpotRepository.findByOlleCourseOrderBySpotOrderAsc(olleCourse);
            List<OlleCourseTag> tags = olleCourseTagRepository.findByOlleCourse(olleCourse);

            saveCourseData(olleCourse, spots, tags);
        }
    }

    private List<OlleCourseTagDocument> setTagDocumentData(List<OlleCourseTag> tags) {
        List<OlleCourseTagDocument> documents = new ArrayList<>();

        for (OlleCourseTag tag : tags) {
            OlleCourseTagDocument olleCourseTagDocument = new OlleCourseTagDocument();
            olleCourseTagDocument.setTitle(tag.getTitle().getTag());
            documents.add(olleCourseTagDocument);
        }

        return documents;
    }

    private List<OlleSpotDocument> setSpotDocumentData(List<OlleSpot> spots) {
        List<OlleSpotDocument> documents = new ArrayList<>();

        for (OlleSpot spot : spots) {
            OlleSpotDocument olleSpotDocument = new OlleSpotDocument();
            olleSpotDocument.setId(spot.getId());
            olleSpotDocument.setTitle(spot.getTitle());
            olleSpotDocument.setLatitude(spot.getLatitude());
            olleSpotDocument.setLongitude(spot.getLongitude());
            olleSpotDocument.setSpotOrder(spot.getSpotOrder());

            documents.add(olleSpotDocument);
        }

        return documents;
    }

    private void saveCourseData(OlleCourse olleCourse, List<OlleSpot> spots, List<OlleCourseTag> tags) {
        List<OlleSpotDocument> spotDocuments = setSpotDocumentData(spots);
        List<OlleCourseTagDocument> tagDocuments = setTagDocumentData(tags);

        OlleCourseDocument olleCourseDocument = new OlleCourseDocument();
        olleCourseDocument.setId(olleCourse.getId());
        olleCourseDocument.setTitle(olleCourse.getTitle());
        olleCourseDocument.setTitleKeyword(olleCourse.getTitle());
        olleCourseDocument.setRoute(olleCourse.getRoute());
        olleCourseDocument.setDistance(olleCourse.getDistance());
        olleCourseDocument.setTime(olleCourse.getTime());
        olleCourseDocument.setSummary(olleCourse.getSummary());
        olleCourseDocument.setContent(olleCourse.getContent());
        olleCourseDocument.setLatitude(olleCourse.getLatitude());
        olleCourseDocument.setLongitude(olleCourse.getLongitude());
        olleCourseDocument.setAddress(olleCourse.getAddress());
        olleCourseDocument.setOpenTime(olleCourse.getOpenTime());
        olleCourseDocument.setTel(olleCourse.getTime());
        olleCourseDocument.setThumbnailUrl(olleCourse.getThumbnailUrl());
        olleCourseDocument.setReviewCount(olleCourse.getReviewCount());
        olleCourseDocument.setStarAvg(olleCourse.getStarAvg());
        olleCourseDocument.setLikeCount(olleCourse.getLikeCount());
        olleCourseDocument.setUpToDate(olleCourse.getUpToDate());
        olleCourseDocument.setSpots(spotDocuments);
        olleCourseDocument.setTags(tagDocuments);

        olleCourseDocumentRepository.save(olleCourseDocument);
    }
}
