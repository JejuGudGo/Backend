package com.gudgo.jeju.global.data.label.repository;

import com.gudgo.jeju.global.data.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Label findByCode(String code);
}
