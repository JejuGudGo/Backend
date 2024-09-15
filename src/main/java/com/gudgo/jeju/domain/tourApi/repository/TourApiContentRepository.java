package com.gudgo.jeju.domain.tourApi.repository;

import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourApiContentRepository extends JpaRepository<TourApiContent, String> {
    List<TourApiContent> findByLatitudeBetweenAndLongitudeBetweenAndContentTypeId(
            double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, String contentTypeId
    );
}
