package com.gudgo.jeju.global.data.tourAPI.spot.repository;

import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourApiSpotDataRepository extends JpaRepository<TourApiSpotData, Long> {
}
