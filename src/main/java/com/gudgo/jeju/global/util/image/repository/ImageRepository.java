package com.gudgo.jeju.global.util.image.repository;

import com.gudgo.jeju.global.util.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
