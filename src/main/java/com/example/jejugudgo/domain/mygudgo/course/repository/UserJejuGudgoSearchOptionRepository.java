package com.example.jejugudgo.domain.mygudgo.course.repository;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSearchOption;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJejuGudgoSearchOptionRepository extends JpaRepository<UserJejuGudgoSearchOption, Long> {
    UserJejuGudgoSearchOption findBySearchOption(SearchOption searchOption);
}

