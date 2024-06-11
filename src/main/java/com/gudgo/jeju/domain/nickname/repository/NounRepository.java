package com.gudgo.jeju.domain.nickname.repository;

import com.gudgo.jeju.domain.nickname.entity.Adjective;
import com.gudgo.jeju.domain.nickname.entity.Noun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NounRepository extends JpaRepository<Noun, Long> {
}
