package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.repository.TrailRepository;
import com.example.jejugudgo.domain.course.search.elastic.doc.TrailDocument;
import com.example.jejugudgo.domain.course.search.elastic.repository.TrailDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TrailElasticDataService {
    private final TrailRepository trailRepository;
    private final TrailDocumentRepository trailDocumentRepository;

    public void createTrailToElastic() {
        List<Trail> trails = trailRepository.findAll();
        for (Trail trail : trails)
            createData(trail);
    }

    private void createData(Trail trail) {
        TrailDocument trailDocument = new TrailDocument();
        trailDocument.setId(trail.getId());
        trailDocument.setTag(trail.getTrailTag().getTag());
        trailDocument.setTitle(trail.getTitle());
        trailDocument.setAddress(trail.getAddress());
        trailDocument.setContent(trail.getContent());
        trailDocument.setTel(trail.getTel());
        trailDocument.setHomepage(trail.getHomepage());
        trailDocument.setOpenTime(trail.getOpenTime());
        trailDocument.setFee(trail.getFee());
        trailDocument.setTime(trail.getTime());
        trailDocument.setThumbnailUrl(trail.getThumbnailUrl());
        trailDocument.setLatitude(trail.getLatitude());
        trailDocument.setLongitude(trail.getLongitude());
        trailDocument.setReviewCount(trail.getReviewCount());
        trailDocument.setStarAvg(trail.getStarAvg());
        trailDocument.setLikeCount(trail.getLikeCount());
        trailDocument.setUpToDate(trail.getUpToDate());

        trailDocumentRepository.save(trailDocument);
    }
}
