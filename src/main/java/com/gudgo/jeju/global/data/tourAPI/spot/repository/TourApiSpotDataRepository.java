package com.gudgo.jeju.global.data.tourAPI.spot.repository;

import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourApiSpotDataRepository extends JpaRepository<TourApiSpotData, Long> {
}