package com.gudgo.jeju.domain.tourApi.repository;

import com.gudgo.jeju.domain.tourApi.entity.TourApiContentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourApiContentImageRepository extends JpaRepository<TourApiContentImage, Long> {
    List<TourApiContentImage> findByTourApiContentInfoId(Long tourId);
}