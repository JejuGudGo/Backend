package com.gudgo.jeju.global.data.tourAPI.common.repository;

import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourApiSubContentTypeRepository extends JpaRepository<TourApiSubContentType, String> {
    List<TourApiSubContentType> findByLatitudeBetweenAndLongitudeBetween(
            double minLatitude, double maxLatitude, double minLongitude, double maxLongitude
    );

}
