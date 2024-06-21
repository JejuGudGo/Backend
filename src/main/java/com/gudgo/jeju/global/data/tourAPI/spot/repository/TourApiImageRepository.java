package com.gudgo.jeju.global.data.tourAPI.spot.repository;

import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourApiImageRepository extends JpaRepository<TourApiSpotImage, Long> {
}