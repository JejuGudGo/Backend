package com.example.jejugudgo.global.api.tourapi.common.repository;

import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiSpots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourApiSpotRepository extends JpaRepository<TourApiSpots, Long> {
    Optional<TourApiSpots> findByContentId(String contentId);
}
